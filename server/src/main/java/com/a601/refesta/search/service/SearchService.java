package com.a601.refesta.search.service;

import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.search.data.AutoCompleteRes;
import com.a601.refesta.search.data.SearchResultRes;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.a601.refesta.artist.domain.QArtist.artist;
import static com.a601.refesta.artist.domain.join.QArtistGenre.artistGenre;
import static com.a601.refesta.festival.domain.QFestival.festival;
import static com.a601.refesta.festival.domain.join.QFestivalGenre.festivalGenre;
import static com.a601.refesta.genre.domain.QGenre.genre;
import static com.a601.refesta.recommendation.domain.QMemberArtist.memberArtist;
import static com.a601.refesta.recommendation.domain.QMemberFestival.memberFestival;

@Slf4j
@Service
public class SearchService {

    private final JPAQueryFactory jpaQueryFactory;

    private final Map<Integer, List<String>> genreMap;
    private final List<String> consonantsList;
    private final Map<Integer, String> syllableMap;

    public SearchService(JPAQueryFactory jpaQueryFactory, Map<Integer, List<String>> genreMap,
                         List<String> consonantsList, Map<Integer, String> syllableMap) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.genreMap = genreMap;
        this.consonantsList = consonantsList;
        this.syllableMap = syllableMap;
    }

    /**
     * 자동완성 검색어 반환
     *
     * @param inputWord
     * @return AutoCompleteRes - 페스티벌 검색어(최대 3개) 리스트, 아티스트 검색어(최대 3개) 리스트
     */
    public AutoCompleteRes getAutoComplete(int memberId, String inputWord) {
        List<AutoCompleteRes.SearchWord> festivalList;
        List<AutoCompleteRes.SearchWord> artistList;

        //검색어가 초성으로 끝나는 경우
        if (checkEndsWithConsonant(inputWord)) {
            String[] fromToWords = setFromToWords(inputWord);

            String fromWord = fromToWords[0];
            String toWord = fromToWords[1];

            //검색어 포함 페스티벌 이름 조회, 추천 순 정렬
            festivalList = jpaQueryFactory
                    .select(Projections.constructor(AutoCompleteRes.SearchWord.class, festival.id, festival.name))
                    .from(festival)
                    .innerJoin(memberFestival).on(memberFestival.member.id.eq(memberId)
                            .and(festival.name.toLowerCase().between(fromWord.toLowerCase(), toWord.toLowerCase()))
                            .and(memberFestival.festival.id.eq(festival.id)))
                    .orderBy(memberFestival.id.asc())
                    .limit(3)
                    .fetch();

            //검색어 포함 아티스트 이름 조회, 추천 순 정렬
            artistList = jpaQueryFactory
                    .select(Projections.constructor(AutoCompleteRes.SearchWord.class, artist.id, artist.name))
                    .from(artist)
                    .leftJoin(memberArtist).on(memberArtist.member.id.eq(memberId)
                            .and(memberArtist.artist.id.eq(artist.id)))
                    .where(artist.name.toLowerCase().between(fromWord.toLowerCase(), toWord.toLowerCase()))
                    .orderBy(memberArtist.id.asc())
                    .limit(3)
                    .fetch();
        } else {
            //페스티벌 이름 포함으로 검색, 일치 >> 시작 >> 추천순 정렬
            festivalList = jpaQueryFactory
                    .select(Projections.constructor(AutoCompleteRes.SearchWord.class, festival.id, festival.name))
                    .from(memberFestival)
                    .innerJoin(festival).on(memberFestival.member.id.eq(memberId)
                            .and(festival.name.toLowerCase().contains(inputWord.toLowerCase()))
                            .and(festival.id.eq(memberFestival.festival.id)))
                    .orderBy(new CaseBuilder()
                                    .when(festival.name.equalsIgnoreCase(inputWord)).then(0)
                                    .when(festival.name.toLowerCase().startsWith(inputWord.toLowerCase())).then(1)
                                    .otherwise(2).asc(),
                            memberFestival.id.asc())
                    .limit(3)
                    .fetch();

            //아티스트 이름 포함으로 검색, 일치 >> 시작 >> 추천 순 정렬
            artistList = jpaQueryFactory
                    .select(Projections.constructor(AutoCompleteRes.SearchWord.class, artist.id, artist.name))
                    .from(artist)
                    .leftJoin(memberArtist).on(memberArtist.member.id.eq(memberId)
                            .and(memberArtist.artist.id.eq(artist.id)))
                    .where(artist.name.toLowerCase().contains(inputWord.toLowerCase()))
                    .orderBy(new CaseBuilder()
                            .when(artist.name.equalsIgnoreCase(inputWord)).then(0)
                            .when(artist.name.toLowerCase().startsWith(inputWord.toLowerCase())).then(1)
                            .otherwise(2).asc(), memberArtist.id.asc())
                    .limit(3)
                    .fetch();
        }

        //조회 정보 반환
        return AutoCompleteRes.builder()
                .festivalWordList(festivalList)
                .artistWordList(artistList)
                .build();
    }

    /**
     * 검색 결과 반환
     *
     * @param searchWord
     * @return SearchResultRes - 페스티벌 리스트(아이디, 이름, 포스터 Url), 아티스트 리스트(아이디, 이름, 사진 Url, 장르 리스트)
     */
    public SearchResultRes getSearchResult(int memberId, String searchWord) {
        List<SearchResultRes.FestivalResult> festivalResultList;
        List<SearchResultRes.ArtistResult> artistResultList;

        if (checkEndsWithConsonant(searchWord)) {
            String[] fromToWords = setFromToWords(searchWord);

            String fromWord = fromToWords[0];
            String toWord = fromToWords[1];

            //검색어 포함 페스티벌 조회(이름), 종료 여부 >> 추천 순 정렬
            festivalResultList = jpaQueryFactory
                    .select(Projections.constructor(SearchResultRes.FestivalResult.class,
                            festival.id, festival.name, festival.posterUrl, festival.isEnded))
                    .from(festival)
                    .innerJoin(memberFestival).on(memberFestival.member.id.eq(memberId)
                            .and(festival.name.toLowerCase().between(fromWord.toLowerCase(), toWord.toLowerCase()))
                            .and(memberFestival.festival.id.eq(festival.id)))
                    .orderBy(new CaseBuilder()
                            .when(festival.isEnded.isFalse()).then(1)
                            .otherwise(2).asc(), memberFestival.id.asc())
                    .fetch();

            //검색어 포함 아티스트 조회(이름), 추천 순 정렬
            artistResultList = jpaQueryFactory
                    .select(Projections.constructor(SearchResultRes.ArtistResult.class,
                            artist.id, artist.name, artist.pictureUrl))
                    .from(artist)
                    .leftJoin(memberArtist).on(memberArtist.member.id.eq(memberId)
                            .and(memberArtist.artist.id.eq(artist.id)))
                    .where(artist.name.toLowerCase().between(fromWord.toLowerCase(), toWord.toLowerCase()))
                    .orderBy(memberArtist.id.asc())
                    .fetch();
        } else {
            //검색어 포함 페스티벌 조회(이름), 일치 >> 시작 >> 종료 여부 >> 추천 순 정렬
            festivalResultList = jpaQueryFactory
                    .select(Projections.constructor(SearchResultRes.FestivalResult.class,
                            festival.id, festival.name, festival.posterUrl, festival.isEnded))
                    .from(festival)
                    .innerJoin(memberFestival).on(memberFestival.member.id.eq(memberId)
                            .and(festival.name.toLowerCase().contains(searchWord.toLowerCase()))
                            .and(festival.id.eq(memberFestival.festival.id)))
                    .orderBy(new CaseBuilder()
                            .when(festival.name.equalsIgnoreCase(searchWord)).then(0)
                            .when(festival.name.toLowerCase().startsWith(searchWord.toLowerCase())).then(1)
                            .when(festival.isEnded.isFalse()).then(2)
                            .otherwise(3).asc(), memberFestival.id.asc())
                    .fetch();

            //검색어 포함 아티스트 조회(이름), 일치 >> 시작 >> 추천 순 정렬
            artistResultList = jpaQueryFactory
                    .select(Projections.constructor(SearchResultRes.ArtistResult.class,
                            artist.id, artist.name, artist.pictureUrl))
                    .from(artist)
                    .leftJoin(memberArtist).on(memberArtist.artist.id.eq(artist.id)
                            .and(memberArtist.member.id.eq(memberId)))
                    .where(artist.name.toLowerCase().contains(searchWord.toLowerCase()))
                    .orderBy(new CaseBuilder()
                            .when(artist.name.equalsIgnoreCase(searchWord)).then(0)
                            .when(artist.name.toLowerCase().startsWith(searchWord.toLowerCase())).then(1)
                            .otherwise(2).asc(), memberArtist.id.asc())
                    .fetch();

            for (int genreIdx = 1; genreIdx < genreMap.size(); genreIdx++) {
                if (genreMap.get(genreIdx).contains(searchWord.toLowerCase())) {
                    //페스티벌 조회(장르) 종료 여부 순 >> 추천 순 정렬
                    List<SearchResultRes.FestivalResult> festivalGenreResult = jpaQueryFactory
                            .select(Projections.constructor(SearchResultRes.FestivalResult.class,
                                    festival.id, festival.name, festival.posterUrl, festival.isEnded))
                            .from(memberFestival)
                            .innerJoin(festival).on(memberFestival.festival.id.eq(festival.id)
                                    .and(memberFestival.member.id.eq(memberId)))
                            .innerJoin(festivalGenre).on(festivalGenre.genre.id.eq(genreIdx)
                                    .and(festivalGenre.festival.id.eq(festival.id)))
                            .orderBy(new CaseBuilder()
                                        .when(festival.isEnded.isFalse()).then(0)
                                        .otherwise(1).asc(), memberFestival.id.asc())
                            .fetch();

                    festivalResultList.addAll(festivalGenreResult);

                    //아티스트 조회(장르) 추천 순 정렬
                    List<SearchResultRes.ArtistResult> artistGenreResult = jpaQueryFactory
                            .select(Projections.constructor(SearchResultRes.ArtistResult.class,
                                    artist.id, artist.name, artist.pictureUrl))
                            .from(memberArtist)
                            .innerJoin(artist).on(memberArtist.artist.id.eq(artist.id)
                                    .and(memberArtist.member.id.eq(memberId)))
                            .innerJoin(artistGenre).on(artistGenre.genre.id.eq(genreIdx)
                                    .and(artistGenre.artist.id.eq(artist.id)))
                            .orderBy(memberArtist.id.asc())
                            .limit(10)
                            .fetch();

                    artistResultList.addAll(artistGenreResult);

                    break;
                }
            }
        }

        for (SearchResultRes.ArtistResult artistResult : artistResultList) {
            //아티스트 장르 조회
            List<String> genreList = jpaQueryFactory
                    .select(genre.name)
                    .from(artistGenre)
                    .innerJoin(genre).on(genre.id.eq(artistGenre.genre.id)
                            .and(artistGenre.artist.id.eq(artistResult.getId())))
                    .fetch();

            artistResult.setGenre(genreList);
        }

        //조회 정보 반환
        return new SearchResultRes(festivalResultList, artistResultList);
    }

    /**
     * 입력어 한글 초성으로 끝나는지 확인
     *
     * @param word
     * @return true/false
     */
    public boolean checkEndsWithConsonant(String word) {
        if (word == null || word.equals("")) {
            throw new CustomException(ErrorCode.SEARCH_WORD_NULL_ERROR);
        }

        Pattern consonants = Pattern.compile("[ㄱ-ㅎ]");
        String endLetter = word.substring(word.length() - 1);
        Matcher matcher = consonants.matcher(endLetter);

        return matcher.find();
    }

    /**
     * 초성 검색 시작, 끝 지정
     *
     * @param word
     * @return String[] - fromWord, toWord
     */
    public String[] setFromToWords(String word) {
        String[] wordArr = new String[2];

        int syllableKey = 0;
        String consonant = word.substring(word.length() - 1);
        for (int consonantIdx = 0; consonantIdx < consonantsList.size(); consonantIdx++) {
            if (consonant.equals(consonantsList.get(consonantIdx))) {
                syllableKey = consonantIdx;
            }
        }

        //ex) ㄱ 입력 시 가 ~ 깋, ㄴ 입력 시 나 ~ 닣
        wordArr[0] = word.substring(0, word.length() - 1) + syllableMap.get(syllableKey);
        wordArr[1] = word.substring(0, word.length() - 1) + syllableMap.get(syllableKey + 1);

        return wordArr;
    }
}
