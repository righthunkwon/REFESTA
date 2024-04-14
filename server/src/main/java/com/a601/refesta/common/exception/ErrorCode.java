package com.a601.refesta.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //Member
    MEMBER_NOT_FOUND_ERROR(404, "MEM001", "존재하지 않는 사용자입니다."),

    //Token
    ACCESS_TOKEN_EXPIRE_ERROR(401, "TOK001", "ACCESS TOKEN이 만료되었습니다."),
    ACCESS_TOKEN_ERROR(401, "TOK002", "Access Token이 잘못되었습니다."),
    REFRESH_TOKEN_VALIDATION_ERROR(401, "TOK003", "Refresh Token이 잘못되었습니다."),

    //Recommendation(member)
    RECOMMENDATION_NOT_READY_ERROR(503, "RCM001", "사용자의 추천 정보를 제공할 수 없습니다."),
    FESTIVAL_LINEUP_NOT_READY_ERROR(503, "RCM002", "페스티벌 라인업 정보를 제공할 수 없습니다."),

    //Festival
    FESTIVAL_NOT_FOUND_ERROR(404, "FES001", "존재하지 않는 페스티벌입니다."),
    FESTIVAL_DETAIL_NOT_READY_ERROR(503, "FES002", "예정된 페스티벌의 상세 정보가 등록되지 않았습니다."),
    FESTIVAL_ALREADY_ENDED_ERROR(400, "FES003", "이미 종료된 페스티벌입니다."),
    FESTIVAL_IS_NOT_ENDED_ERROR(400, "FES004", "아직 종료되지 않은 페스티벌입니다."),
    FESTIVAL_SETLIST_NOT_READY_ERROR(503, "FES005", "페스티벌 셋리스트 정보를 제공할 수 없습니다."),

    //Artist
    ARTIST_NOT_FOUND_ERROR(404, "ART001", "존재하지 않는 아티스트입니다."),

    //Review 
    REVIEW_MEMBER_NOT_EQUAL(404, "REW001", "작성자와 회원정보가 일치하지 않습니다."),

    //Search
    SEARCH_WORD_NULL_ERROR(400, "SCH001", "검색어가 입력되지 않았습니다."),

    //KakaoPay
    KAKAOPAY_FAILED_ERROR(400, "PAY001", "결제 승인에 실패했습니다."),

    //Reservation
    RESERVATION_MEMBER_NOT_EQUAL(404, "REV001", "예매자와 회원정보가 일치하지 않습니다."),
    RESERVATION_NOT_FOUND_ERROR(404, "REV002", "존재하지 않는 예매내역입니다."),

    //Song
    SONG_NOT_FOUND_ERROR(404, "SNG001", "존재하지 않는 노래입니다."),

    //Playlist
    PLAYLIST_SETLIST_NULL_ERROR(400,"PLL001","선택한 셋리스트가 없습니다.");

    private final int status;

    private final String code;

    private final String message;
}
