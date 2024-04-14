package com.a601.refesta.member.service;

import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.common.util.S3Util;
import com.a601.refesta.genre.repository.GenreRepository;
import com.a601.refesta.login.repository.GoogleAccessTokenRepository;
import com.a601.refesta.member.data.*;
import com.a601.refesta.member.domain.Member;
import com.a601.refesta.member.domain.join.MemberGenre;
import com.a601.refesta.member.domain.join.MemberSongPreference;
import com.a601.refesta.member.repository.MemberGenreRepository;
import com.a601.refesta.member.repository.MemberRepository;
import com.a601.refesta.member.repository.MemberSongRepository;
import com.a601.refesta.song.repository.SongRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.a601.refesta.artist.domain.QArtist.artist;
import static com.a601.refesta.festival.domain.QFestival.festival;
import static com.a601.refesta.member.domain.QMember.member;
import static com.a601.refesta.member.domain.join.QArtistLike.artistLike;
import static com.a601.refesta.member.domain.join.QFestivalLike.festivalLike;
import static com.a601.refesta.reservation.domain.QReservation.reservation;
import static com.a601.refesta.review.domain.QReview.review;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGenreRepository memberGenreRepository;
    private final GenreRepository genreRepository;
    private final S3Util s3Util;
    private final JPAQueryFactory jpaQueryFactory;
    private final SongRepository songRepository;
    private final MemberSongRepository memberSongRepository;
    private final GoogleAccessTokenRepository googleAccessTokenRepository;

    @Value("${spring.refesta.recommend.url}")
    private String REFESTA_URL;

    @Value("${spring.security.oauth2.provider.google.api-key}")
    private String API_KEY;

    public Member getMember(int memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    }

    public MemberProfileRes getProfile(int memberId) {
        Member member = getMember(memberId);
        return MemberProfileRes.builder()
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    /**
     * 프로필 저장
     */
    public void updateProfile(int memberId, String nickname, MultipartFile file) {
        Member member = getMember(memberId);
        member.setNickname(nickname);
        if (file != null && !file.isEmpty()) {
            member.setProfileUrl(s3Util.uploadFile(file));
        }
        memberRepository.save(member);
    }

    /**
     * 선호장르 저장
     */
    public void createPreferGenre(int memberId, PreferGenreReq genres) {

        Member member = getMember(memberId);
        memberGenreRepository.deleteAllByMember(member);
        if (genres.getPreferGenres() != null && !genres.getPreferGenres().isEmpty()) {
            for (Integer genreId : genres.getPreferGenres()) {
                MemberGenre memberGenre =
                        MemberGenre.builder()
                                .genre(genreRepository.findById(genreId).orElseThrow())
                                .member(member)
                                .build();
                memberGenreRepository.save(memberGenre);
            }
        }

        //추천 데이터 업데이트 요청
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String, Integer> parameters = new LinkedMultiValueMap<>();
        parameters.add("userId", memberId);

        ResponseEntity<String> response = rt.postForEntity(
                REFESTA_URL + "/recommend",
                parameters,
                String.class
        );
        String res = response.getBody();
        if (res == null || !res.equals("추천 완료")) {
            throw new CustomException(ErrorCode.RECOMMENDATION_NOT_READY_ERROR);
        }
    }


    /**
     * 좋아요한 페스티벌 목록
     *
     * @return List<LikeFestivalRes> - 페스티벌(아이디, 사진, 이름, 좋아요여부)
     */
    public List<LikeFestivalRes> getLikeFestivals(int memberId) {

        return jpaQueryFactory.select(Projections.constructor(
                        LikeFestivalRes.class, festival.id, festival.posterUrl,
                        festival.name, festivalLike.isLiked, festival.isEnded))
                .from(festival)
                .innerJoin(festivalLike).on(festivalLike.festival.id.eq(festival.id))
                .innerJoin(member).on(festivalLike.member.id.eq(member.id))
                .where(member.id.eq(memberId), festivalLike.isLiked.eq(true))
                .orderBy(festivalLike.lastModifiedDate.desc())
                .fetch();
    }

    /**
     * 좋아요한 아티스트 목록
     *
     * @return List<LikeArtistRes> - 아티스트(아이디, 사진, 이름, 좋아요여부)
     */
    public List<LikeArtistRes> getLikeArtists(int memberId) {

        return jpaQueryFactory.select(Projections.constructor(
                        LikeArtistRes.class, artist.id, artist.pictureUrl,
                        artist.name, artistLike.isLiked))
                .from(artist)
                .innerJoin(artistLike).on(artistLike.artist.id.eq(artist.id))
                .innerJoin(member).on(artistLike.member.id.eq(member.id))
                .where(member.id.eq(memberId), artistLike.isLiked.eq(true))
                .orderBy(artistLike.lastModifiedDate.desc())
                .fetch();
    }

    /**
     * 내 예매내역 조회
     *
     * @return List<ReservationRes> - 예약 아이디, 페스티벌 (이름, 날짜, 장소)
     */
    public List<ReservationRes> getReservations(int memberId) {

        return jpaQueryFactory.select(Projections.constructor(
                        ReservationRes.class, reservation.id, festival.name,
                        festival.festivalDate, festival.location))
                .from(festival)
                .innerJoin(reservation).on(reservation.festival.id.eq(festival.id))
                .innerJoin(member).on(reservation.member.id.eq(memberId))
                .where(member.id.eq(memberId), reservation.status.eq("SUCCESS"))
                .orderBy(festival.festivalDate.desc())
                .fetch();
    }

    /**
     * 내가 쓴 후기 조회
     *
     * @return List<ReviewRes> - 리뷰 (아이디, 글, 첨부파일, 파일형식), 페스티벌 (이름, 날짜, 장소)
     */
    public List<ReviewRes> getReviews(int memberId) {

        return jpaQueryFactory.select(Projections.constructor(
                        ReviewRes.class, review.id, festival.name,
                        festival.festivalDate, festival.location, review.contents,
                        review.attachmentUrl, review.mediaType))
                .from(review)
                .innerJoin(member)
                .on(review.member.id.eq(member.id))
                .innerJoin(festival)
                .on(review.festival.id.eq(festival.id))
                .where(member.id.eq(memberId), review.isDeleted.eq(false))
                .orderBy(review.createdDate.desc())
                .fetch();

    }

    /**
     * 플리 음악 MemberSongPreference에 넣기
     */
    public void createMemberSongPreference(int memberId) {

        memberSongRepository.deleteAllByMemberId(memberId);

        String googleAccessToken = googleAccessTokenRepository.findById(String.valueOf(memberId)).orElseThrow().getGoogleAccessToken();
        // 유저의 유튜브 채널 ID 다 가져오기
        List<String> channelIds = getMemberYoutubeChannels(googleAccessToken);
        if (channelIds != null) {
            //채널 ID의 재생목록 가져오기
            List<String> playlists = getMemberYoutubePlaylists(channelIds);
            if (playlists != null) {
                //재생목록의 노래들 가져오기
                List<String> videoIds = getMemberYoutubeSongs(playlists);
                if (videoIds != null && !videoIds.isEmpty()) {
                    //곡이랑 비교 후 membersongpreference 테이블에 넣기
                    for (String videoId : videoIds) {
                        songRepository.findByVideoId("https://www.youtube.com/watch?v=" + videoId + "%")
                                .ifPresent(song -> {
                                    memberSongRepository.findByMember_IdAndSong_Id(memberId, song.getId()).ifPresent(
                                            memberSongPreference -> {
                                                memberSongPreference.plusPlayListPreference();
                                                memberSongRepository.save(memberSongPreference);
                                            }
                                    );
                                    if (memberSongRepository.findByMember_IdAndSong_Id(memberId, song.getId()).isEmpty()) {
                                        memberSongRepository.save(MemberSongPreference.builder()
                                                .song(song)
                                                .member(memberRepository.findById(memberId).orElseThrow())
                                                .preference(30)
                                                .build());
                                    }
                                });
                    }
                }
            }
        }
    }

    private List<String> getMemberYoutubeSongs(List<String> playlists) {
        if (playlists.isEmpty()) return null;
        RestTemplate rt = new RestTemplate();
        List<String> videoIds = new ArrayList<>();
        for (String playlist : playlists) {
            ResponseEntity<String> response = rt.exchange(
                    "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=100&status=&playlistId=" + playlist + "&key=" + API_KEY,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            try {
                // JSON 문자열을 객체로 매핑하기 위한 ObjectMapper 생성
                ObjectMapper objectMapper = new ObjectMapper();

                // ResponseEntity의 body를 JSON 문자열로부터 JsonNode 객체로 변환
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                // "items" 배열의 각 객체에서 "videoId" 값을 추출하여 리스트에 저장
                for (JsonNode item : jsonNode.get("items")) {
                    String videoId = item.get("snippet").get("resourceId").get("videoId").asText();
                    videoIds.add(videoId);
                }
            } catch (JsonProcessingException e) {
                // JSON 처리 예외가 발생
                e.printStackTrace();
            }
        }
        return videoIds;
    }

    private List<String> getMemberYoutubePlaylists(List<String> channelIds) {
        if (channelIds.isEmpty()) return null;
        RestTemplate rt = new RestTemplate();
        List<String> playlists = new ArrayList<>();
        for (String channelId : channelIds) {
            ResponseEntity<String> response = rt.exchange(
                    "https://youtube.googleapis.com/youtube/v3/playlists?channelId=" + channelId + "&key=" + API_KEY,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // ResponseEntity의 body를 JSON 문자열로부터 JsonNode 객체로 변환
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                for (JsonNode item : jsonNode.get("items")) {
                    playlists.add(item.get("id").asText());
                }

            } catch (JsonProcessingException e) {
                // JSON 처리 예외가 발생
                e.printStackTrace();
            }
        }
        return playlists;
    }

    private List<String> getMemberYoutubeChannels(String googleAccessToken) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://youtube.googleapis.com/youtube/v3/channels?part=statistics&mine=true&key=" + API_KEY,
                HttpMethod.GET,
                req,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // ResponseEntity의 body를 JSON 문자열로부터 JsonNode 객체로 변환
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            List<String> channelIds = new ArrayList<>();
            for (JsonNode item : jsonNode.get("items")) {
                channelIds.add(item.get("id").asText());
            }
            return channelIds;
        } catch (JsonProcessingException e) {
            // JSON 처리 예외가 발생
            e.printStackTrace();
            return null;
        }

    }
}
