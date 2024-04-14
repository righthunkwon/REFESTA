package com.a601.refesta.recommendation.service;

import com.a601.refesta.artist.domain.Artist;
import com.a601.refesta.artist.service.ArtistService;
import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.festival.domain.Festival;
import com.a601.refesta.festival.service.FestivalService;
import com.a601.refesta.member.domain.join.MemberSongPreference;
import com.a601.refesta.member.repository.MemberSongRepository;
import com.a601.refesta.member.service.MemberService;
import com.a601.refesta.recommendation.data.ArtistRecommendationRes;
import com.a601.refesta.recommendation.data.EntireFestivalInfoRes;
import com.a601.refesta.recommendation.data.FestivalRecommendationRes;
import com.a601.refesta.recommendation.domain.MemberArtist;
import com.a601.refesta.recommendation.domain.MemberArtistPreference;
import com.a601.refesta.recommendation.domain.MemberFestival;
import com.a601.refesta.recommendation.domain.MemberFestivalPreference;
import com.a601.refesta.recommendation.repository.MemberArtistPreferenceRepository;
import com.a601.refesta.recommendation.repository.MemberArtistRepository;
import com.a601.refesta.recommendation.repository.MemberFestivalPreferenceRepository;
import com.a601.refesta.recommendation.repository.MemberFestivalRepository;
import com.a601.refesta.song.domain.Song;
import com.a601.refesta.song.repository.SongRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.a601.refesta.artist.domain.QArtist.artist;
import static com.a601.refesta.festival.domain.join.QFestivalLineup.festivalLineup;
import static com.a601.refesta.recommendation.domain.QMemberArtist.memberArtist;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MemberService memberService;
    private final FestivalService festivalService;
    private final ArtistService artistService;

    private final SongRepository songRepository;
    private final MemberFestivalRepository memberFestivalRepository;
    private final MemberArtistRepository memberArtistRepository;
    private final MemberFestivalPreferenceRepository memberFestivalPreferenceRepository;
    private final MemberArtistPreferenceRepository memberArtistPreferenceRepository;
    private final MemberSongRepository memberSongRepository;

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 사용자 추천 페스티벌, 셋리스트 조회
     *
     * @param memberId
     * @return RecommendedFestivalRes - 예정 페스티벌 리스트(아이디, 이름, 날짜, 포스터 Url), 종료 페스티벌 리스트(+ 라인업)
     */
    public FestivalRecommendationRes getFestivalRecommendation(int memberId) {
        List<MemberFestival> findRecommendation = getMemberFestival(memberId);

        List<FestivalRecommendationRes.ScheduledFestival> scheduledFestivalList = new ArrayList<>();
        List<FestivalRecommendationRes.EndedFestival> endedFestivalList = new ArrayList<>();
        for (MemberFestival memberFestival : findRecommendation) {
            Festival findFestival = memberFestival.getFestival();

            //예정 페스티벌 저장(8개)
            if (!findFestival.getIsEnded() && scheduledFestivalList.size() < 8) {
                scheduledFestivalList.add(new FestivalRecommendationRes.ScheduledFestival(
                        findFestival.getId(), findFestival.getName(), findFestival.getFestivalDate(), findFestival.getPosterUrl()));
            }

            //종료 페스티벌 저장(8개)
            else if (findFestival.getIsEnded() && endedFestivalList.size() < 8) {
                //정보 저장
                endedFestivalList.add(new FestivalRecommendationRes.EndedFestival(findFestival.getId(), findFestival.getName(),
                        findFestival.getFestivalDate(), findFestival.getPosterUrl(), getLineup(memberId, findFestival.getId())));
            }

            //페스티벌 예정, 종료 8개씩 찾고 종료
            if (scheduledFestivalList.size() == 8 && endedFestivalList.size() == 8) {
                break;
            }
        }

        return FestivalRecommendationRes.builder()
                .scheduledFestivalList(scheduledFestivalList)
                .endedFestivalList(endedFestivalList)
                .build();
    }

    /**
     * 사용자 추천 아티스트 조회
     *
     * @param memberId
     * @param pageNo   - 조회 페이지 번호(client 새로고침 횟수)
     * @return ArtistRecommendationREs - 아이디, 이름, 사진 Url
     */
    public ArtistRecommendationRes getArtistRecommendation(int memberId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 8);

        Page<MemberArtist> findRecommendation = memberArtistRepository.findAllByMember_Id(memberId, pageable);

        if (findRecommendation.isEmpty()) {
            throw new CustomException(ErrorCode.RECOMMENDATION_NOT_READY_ERROR);
        }

        List<ArtistRecommendationRes.ArtistInfo> artistInfoList = new ArrayList<>();
        for (MemberArtist memberArtist : findRecommendation) {
            Artist findArtist = memberArtist.getArtist();

            artistInfoList.add(new ArtistRecommendationRes.ArtistInfo
                    (findArtist.getId(), findArtist.getName(), findArtist.getPictureUrl()));
        }

        return ArtistRecommendationRes.builder()
                .artistInfoList(artistInfoList)
                .build();
    }

    /**
     * 예정 페스티벌 전체 조회
     *
     * @param memberId
     * @return List<EntireFestivalInfoRes> - 아이디, 이름, 날짜, 장소, 포스터 Url, 라인업
     */
    public List<EntireFestivalInfoRes> getEntireScheduledFestival(int memberId) {
        List<MemberFestival> findRecommendation = memberFestivalRepository.findAllByMember_Id(memberId);

        List<EntireFestivalInfoRes> festivalInfoList = new ArrayList<>();
        for (MemberFestival memberFestival : findRecommendation) {
            Festival findFestival = memberFestival.getFestival();

            //예정 페스티벌 저장
            if (!findFestival.getIsEnded()) {
                festivalInfoList.add(new EntireFestivalInfoRes(findFestival.getId(), findFestival.getName(),
                        findFestival.getFestivalDate(), findFestival.getLocation(), findFestival.getPosterUrl(), getLineup(memberId, findFestival.getId())));
            }
        }

        return festivalInfoList;
    }

    /**
     * 종료 페스티벌 전체 조회
     *
     * @param memberId
     * @return List<EntireFestivalInfoRes> - 아이디, 이름, 날짜, 장소, 포스터 Url, 라인업
     */
    public List<EntireFestivalInfoRes> getEntireEndedFestival(int memberId) {
        List<MemberFestival> findRecommendation = memberFestivalRepository.findAllByMember_Id(memberId);

        List<EntireFestivalInfoRes> festivalInfoList = new ArrayList<>();
        for (MemberFestival memberFestival : findRecommendation) {
            Festival findFestival = memberFestival.getFestival();

            //예정 페스티벌 저장
            if (findFestival.getIsEnded()) {
                festivalInfoList.add(new EntireFestivalInfoRes(findFestival.getId(), findFestival.getName(),
                        findFestival.getFestivalDate(), findFestival.getLocation(), findFestival.getPosterUrl(), getLineup(memberId, findFestival.getId())));
            }
        }

        return festivalInfoList;
    }

    /**
     * 사용자 추천 페스티벌 라인업 조회
     *
     * @param memberId
     * @param festivalId
     * @return "lineup"
     */
    public String getLineup(int memberId, int festivalId) {
        //페스티벌 라인업에서 추천 아티스트 조회(4명)
        List<String> findLineup = jpaQueryFactory.select(artist.name)
                .from(memberArtist)
                .innerJoin(festivalLineup).on(festivalLineup.artist.id.eq(memberArtist.artist.id)
                        .and(festivalLineup.festival.id.eq(festivalId)))
                .innerJoin(artist).on(artist.id.eq(memberArtist.artist.id))
                .where(memberArtist.member.id.eq(memberId))
                .limit(4)
                .fetch();

        //라인업 StringBuilder로 변환
        StringBuilder lineup = new StringBuilder();

        if(!findLineup.isEmpty()){
            for (String artistName : findLineup) {
                lineup.append(artistName).append(",");
            }
            lineup.deleteCharAt(lineup.length() - 1);
        }

        return lineup.toString();
    }

    /**
     * 사용자 추천 페스티벌 테이블 조회
     *
     * @param memberId
     * @return List<MemberFestival>
     */
    public List<MemberFestival> getMemberFestival(int memberId) {
        List<MemberFestival> findRecommendation = memberFestivalRepository.findAllByMember_Id(memberId);

        if (findRecommendation.isEmpty()) {
            throw new CustomException(ErrorCode.RECOMMENDATION_NOT_READY_ERROR);
        }

        return findRecommendation;
    }

    public HttpStatus updateFestivalPreference(int memberId, int festivalId, int point) {
        Optional<MemberFestivalPreference> optFindPreference = memberFestivalPreferenceRepository
                .findByMember_IdAndFestival_Id(memberId, festivalId);

        //DB에 없으면 추가
        if (optFindPreference.isEmpty()) {
            memberFestivalPreferenceRepository.save(MemberFestivalPreference.builder()
                    .member(memberService.getMember(memberId))
                    .festival(festivalService.getFestival(festivalId))
                    .preference(point)
                    .build()
            );

            return HttpStatus.CREATED;
        }

        //DB에 있으면 좋아요 상태 업데이트
        MemberFestivalPreference findPreference = optFindPreference.get();
        findPreference.updatePreference(point);
        memberFestivalPreferenceRepository.save(findPreference);

        return HttpStatus.OK;
    }

    public HttpStatus updateSongPreference(int memberId, int songId) {
        Optional<MemberSongPreference> optFindMemberSong = memberSongRepository.findByMember_IdAndSong_Id(memberId, songId);

        //DB에 없으면 추가
        if (optFindMemberSong.isEmpty()) {
            Song song = songRepository.findById(songId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SONG_NOT_FOUND_ERROR));

            memberSongRepository.save(MemberSongPreference.builder()
                    .member(memberService.getMember(memberId))
                    .song(song)
                    .preference(1)
                    .build()
            );

            return HttpStatus.CREATED;
        }

        //DB에 있으면 좋아요 상태 업데이트
        MemberSongPreference findPreference = optFindMemberSong.get();
        findPreference.updatePreference();
        memberSongRepository.save(findPreference);

        return HttpStatus.OK;
    }

    public HttpStatus updateArtistPreference(int memberId, int artistId, int point) {
        Optional<MemberArtistPreference> optFindPreference = memberArtistPreferenceRepository
                .findByMember_IdAndArtist_Id(memberId, artistId);

        //DB에 없으면 추가
        if (optFindPreference.isEmpty()) {
            memberArtistPreferenceRepository.save(MemberArtistPreference.builder()
                    .member(memberService.getMember(memberId))
                    .artist(artistService.getArtist(artistId))
                    .preference(point)
                    .build()
            );

            return HttpStatus.CREATED;
        }

        //DB에 있으면 좋아요 상태 업데이트
        MemberArtistPreference findPreference = optFindPreference.get();
        findPreference.updatePreference(point);
        memberArtistPreferenceRepository.save(findPreference);

        return HttpStatus.OK;
    }
}
