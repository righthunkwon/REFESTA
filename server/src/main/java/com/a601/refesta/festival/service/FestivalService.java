package com.a601.refesta.festival.service;

import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.festival.data.*;
import com.a601.refesta.festival.domain.Festival;
import com.a601.refesta.festival.domain.FestivalDetail;
import com.a601.refesta.festival.repository.FestivalDetailRepository;
import com.a601.refesta.festival.repository.FestivalRepository;
import com.a601.refesta.login.repository.GoogleAccessTokenRepository;
import com.a601.refesta.member.domain.join.FestivalLike;
import com.a601.refesta.member.repository.FestivalLikeRepository;
import com.a601.refesta.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.a601.refesta.artist.domain.QArtist.artist;
import static com.a601.refesta.artist.domain.join.QArtistSong.artistSong;
import static com.a601.refesta.festival.domain.QFestival.festival;
import static com.a601.refesta.festival.domain.join.QFestivalLineup.festivalLineup;
import static com.a601.refesta.festival.domain.join.QFestivalSetlist.festivalSetlist;
import static com.a601.refesta.member.domain.QMember.member;
import static com.a601.refesta.member.domain.join.QFestivalLike.festivalLike;
import static com.a601.refesta.review.domain.QReview.review;
import static com.a601.refesta.song.domain.QSong.song;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final MemberService memberService;

    private final FestivalRepository festivalRepository;
    private final FestivalDetailRepository festivalDetailRepository;
    private final FestivalLikeRepository festivalLikeRepository;
    private final GoogleAccessTokenRepository googleAccessTokenRepository;

    private final JPAQueryFactory jpaQueryFactory;


    @Value("${spring.security.oauth2.provider.google.api-key}")
    private String API_KEY;

    /**
     * 페스티벌(공통) 정보 조회
     *
     * @param festivalId
     * @return FestivalInfoRes - 이름, 날짜, 장소, 포스터 URL, 가격
     */
    public FestivalInfoRes getFestivalInfo(int memberId, int festivalId) {
        //기본 정보 반환
        return jpaQueryFactory
                .select(Projections.constructor(FestivalInfoRes.class, festival.id, festival.name, festival.festivalDate,
                        festival.location, festival.posterUrl, festival.price, festival.isEnded, festivalLike.isLiked))
                .from(festival)
                .leftJoin(festivalLike).on(festival.id.eq(festivalLike.festival.id)
                        .and(festivalLike.member.id.eq(memberId)))
                .where(festival.id.eq(festivalId))
                .fetchOne();
    }

    /**
     * 페스티벌(공통) 좋아요 업데이트
     *
     * @param memberId
     * @param festivalId
     */
    public HttpStatus updateFestivalLike(int memberId, int festivalId) {
        Optional<FestivalLike> optFindLike = festivalLikeRepository
                .findByMember_IdAndFestival_Id(memberId, festivalId);

        //DB에 없으면 추가
        if (optFindLike.isEmpty()) {
            festivalLikeRepository.save(FestivalLike.builder()
                    .member(memberService.getMember(memberId))
                    .festival(getFestival(festivalId))
                    .isLiked(true)
                    .build()
            );

            return HttpStatus.CREATED;
        }

        //DB에 있으면 좋아요 상태 업데이트
        FestivalLike findLike = optFindLike.get();
        findLike.updateStatus();
        festivalLikeRepository.save(findLike);

        return HttpStatus.OK;
    }

    /**
     * 페스티벌(예정) 상세 정보 조회
     *
     * @param festivalId
     * @return FestivalDetailRes - 상세 정보 URL
     */
    public FestivalDetailRes getFestivalDetail(int festivalId) {
        checkIsScheduled(getFestival(festivalId));

        FestivalDetail findDetail = festivalDetailRepository.findByFestival_Id(festivalId)
                .orElseThrow(() -> new CustomException(ErrorCode.FESTIVAL_DETAIL_NOT_READY_ERROR));

        //상세 정보 이미지 반환
        return FestivalDetailRes.builder()
                .infoImgUrl(findDetail.getInfoImgUrl())
                .build();
    }

    /**
     * 페스티벌(종료) 셋리스트 조회
     *
     * @param festivalId
     * @return FestivalSetlistRes - 아티스트(아이디, 이름, 사진 Url) 리스트, 노래(제목, 사진 Url, 음원 Url, 가수 이름) 리스트
     */
    public FestivalSetlistRes getFestivalSetlist(int festivalId) {
        checkIsEnded(getFestival(festivalId));

        List<Tuple> searchResult = jpaQueryFactory
                .select(song.id, song.title, song.audioUrl, song.imageUrl, artist.id, artist.name, artist.pictureUrl)
                .from(festivalSetlist)
                .innerJoin(festivalLineup).on(festivalSetlist.festival.id.eq(festivalLineup.festival.id))
                .innerJoin(artistSong).on(festivalLineup.artist.id.eq(artistSong.artist.id)
                        .and(artistSong.song.id.eq(festivalSetlist.song.id)))
                .innerJoin(song).on(artistSong.song.id.eq(song.id))
                .innerJoin(artist).on(artistSong.artist.id.eq(artist.id))
                .where(festivalSetlist.festival.id.eq(festivalId))
                .orderBy(festivalSetlist.id.asc())
                .fetch();

        if (searchResult.isEmpty()) {
            throw new CustomException(ErrorCode.FESTIVAL_SETLIST_NOT_READY_ERROR);
        }

        List<FestivalSetlistRes.ArtistInfo> lineupList = new ArrayList<>();
        Map<Integer, List<FestivalSetlistRes.SongInfo>> songInfoMap = new HashMap<>();
        for (Tuple tuple : searchResult) {
            int artistId = tuple.get(artist.id);

            //새로운 아티스트 정보를 lineup에 저장, songInfoMap의 Key로 추가
            if (!songInfoMap.containsKey(artistId)) {
                lineupList.add(new FestivalSetlistRes.ArtistInfo
                        (artistId, tuple.get(artist.name), tuple.get(artist.pictureUrl)));
                songInfoMap.put(artistId, new ArrayList<>());
            }

            //노래 정보 저장
            List<FestivalSetlistRes.SongInfo> songInfoList = songInfoMap.get(artistId);
            songInfoList.add(new FestivalSetlistRes.SongInfo(tuple.get(song.id), tuple.get(song.title),
                    tuple.get(song.audioUrl), tuple.get(song.imageUrl), tuple.get(artist.name)));
        }

        //셋리스트 정보 반환
        return FestivalSetlistRes.builder()
                .lineupList(lineupList)
                .songInfoMap(songInfoMap)
                .build();
    }

    /**
     * 페스티벌(종료) 후기 조회
     *
     * @param festivalId
     * @return List<FestivalReviewRes> - 작성자 닉네임, 작성자 프로필, 첨부파일 Url, 미디어타입, 내용
     */
    public List<FestivalReviewRes> getFestivalReview(int festivalId) {
        checkIsEnded(getFestival(festivalId));

        //작성자 정보, Review 정보 반환
        return jpaQueryFactory
                .select(Projections.constructor(FestivalReviewRes.class, member.nickname, member.profileUrl,
                        review.attachmentUrl, review.mediaType, review.contents))
                .from(review)
                .innerJoin(member).on(review.member.id.eq(member.id))
                .where(review.festival.id.eq(festivalId).and(review.isDeleted.eq(false)))
                .orderBy(review.id.desc())
                .fetch();
    }

    /**
     * 페스티벌 종료 여부 확인
     *
     * @param findFestival
     */
    public void checkIsEnded(Festival findFestival) {
        if (!findFestival.getIsEnded()) {
            throw new CustomException(ErrorCode.FESTIVAL_IS_NOT_ENDED_ERROR);
        }
    }

    /**
     * 페스티벌 예정 여부 확인
     *
     * @param findFestival
     */
    public void checkIsScheduled(Festival findFestival) {
        if (findFestival.getIsEnded()) {
            throw new CustomException(ErrorCode.FESTIVAL_ALREADY_ENDED_ERROR);
        }
    }

    /**
     * 페스티벌 조회
     *
     * @param festivalId
     * @return Festival
     */
    public Festival getFestival(int festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(() -> new CustomException(ErrorCode.FESTIVAL_NOT_FOUND_ERROR));
    }


    public void createYoutubePlaylist(int memberId, FestivalSetlistReq festivalSetlistReq) {
        String googleAccessToken = googleAccessTokenRepository.findById(String.valueOf(memberId)).orElseThrow().getGoogleAccessToken();
        if (festivalSetlistReq.getAudioUrlList().isEmpty())
            throw new CustomException(ErrorCode.PLAYLIST_SETLIST_NULL_ERROR);
        String playlistId = createPlaylist(googleAccessToken, festivalSetlistReq.getFestivalName());
        putSongInPlaylist(googleAccessToken, playlistId, festivalSetlistReq.getAudioUrlList());
    }


    private void putSongInPlaylist(String googleAccessToken, String playlistId, List<String> audioUrlList) {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        for (String audioUrl : audioUrlList) {
            audioUrl = audioUrl.replace("https://www.youtube.com/watch?v=", "");
            String[] split = audioUrl.split("&");

            Map<String, Object> requestBody = new LinkedHashMap<>();

            Map<String, Object> snippetMap = new LinkedHashMap<>();
            snippetMap.put("playlistId", playlistId);

            Map<String, Object> resourceIdMap = new LinkedHashMap<>();
            resourceIdMap.put("kind", "youtube#video");
            resourceIdMap.put("videoId", split[0]);

            snippetMap.put("resourceId", resourceIdMap);
            requestBody.put("snippet", snippetMap);

            HttpEntity<Map<String, Object>> req = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = rt.exchange(
                    "https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&key=" + API_KEY,
                    HttpMethod.POST,
                    req,
                    String.class
            );

        }
    }


    private String createPlaylist(String googleAccessToken, String festivalName) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("kind", "youtube#playlist");

        Map<String, Object> snippetMap = new LinkedHashMap<>();
        snippetMap.put("title", "ReFesta-" + festivalName);
        requestBody.put("snippet", snippetMap);


        HttpEntity<Map<String, Object>> req = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://youtube.googleapis.com/youtube/v3/playlists?part=snippet&key=" + API_KEY,
                HttpMethod.POST,
                req,
                String.class
        );
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // ResponseEntity의 body를 JSON 문자열로부터 JsonNode 객체로 변환
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("id").asText();
        } catch (JsonProcessingException e) {
            // JSON 처리 예외가 발생
            e.printStackTrace();
            return null;
        }
    }
}
