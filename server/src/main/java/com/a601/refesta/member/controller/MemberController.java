package com.a601.refesta.member.controller;

import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import com.a601.refesta.member.data.*;
import com.a601.refesta.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public SuccessResponse<MemberProfileRes> getProfile(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        MemberProfileRes memberProfileRes = memberService.getProfile(memberId);
        return new SuccessResponse<>(memberProfileRes);
    }

    @PostMapping
    public SuccessResponse<Integer> editProfile(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("nickname") String nickname) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        memberService.updateProfile(memberId, nickname, file);
        return new SuccessResponse<>(HttpStatus.SC_OK);
    }

    @PostMapping("/genres")
    public SuccessResponse<Integer> addPreferGenre(HttpServletRequest request, @RequestBody PreferGenreReq genres) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        memberService.createPreferGenre(memberId, genres);
        return new SuccessResponse<>(HttpStatus.SC_OK);
    }

    @GetMapping("/festivals")
    public SuccessResponse<Map<String, List<LikeFestivalRes>>> getLikeFestivals(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        Map<String, List<LikeFestivalRes>> data = new TreeMap<>();
        data.put("festivalList", memberService.getLikeFestivals(memberId));
        return new SuccessResponse<>(data);
    }

    @GetMapping("/artists")
    public SuccessResponse<Map<String, List<LikeArtistRes>>> getLikeArtists(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        Map<String, List<LikeArtistRes>> data = new TreeMap<>();
        data.put("artistList", memberService.getLikeArtists(memberId));
        return new SuccessResponse<>(data);
    }

    @GetMapping("/reservations")
    public SuccessResponse<Map<String, List<ReservationRes>>> getReservations(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        Map<String, List<ReservationRes>> data = new TreeMap<>();
        data.put("reservationList", memberService.getReservations(memberId));
        return new SuccessResponse<>(data);
    }

    @GetMapping("/reviews")
    public SuccessResponse<Map<String, List<ReviewRes>>> getReviews(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        Map<String, List<ReviewRes>> data = new TreeMap<>();
        data.put("reviewList", memberService.getReviews(memberId));
        return new SuccessResponse<>(data);
    }

    @PostMapping("/playlists")
    public SuccessResponse<Integer> addMemberSongPreference(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        memberService.createMemberSongPreference(memberId);
        return new SuccessResponse<>(HttpStatus.SC_CREATED);
    }
}
