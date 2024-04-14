package com.a601.refesta.festival.controller;

import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import com.a601.refesta.festival.data.*;
import com.a601.refesta.festival.service.FestivalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("festivals")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final TokenProvider tokenProvider;

    @GetMapping("/{festival_id}")
    public SuccessResponse<FestivalInfoRes> getFestivalInfo(HttpServletRequest request,
                                                            @PathVariable(name = "festival_id") int festivalId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(festivalService.getFestivalInfo(memberId, festivalId));
    }

    @GetMapping("/{festival_id}/info")
    public SuccessResponse<FestivalDetailRes> getFestivalDetail(@PathVariable(name = "festival_id") int festivalId) {
        return new SuccessResponse<>(festivalService.getFestivalDetail(festivalId));
    }

    @GetMapping("/{festival_id}/songs")
    public SuccessResponse<FestivalSetlistRes> getFestivalSetlist(@PathVariable(name = "festival_id") int festivalId) {
        return new SuccessResponse<>(festivalService.getFestivalSetlist(festivalId));
    }

    @GetMapping("/{festival_id}/reviews")
    public SuccessResponse<List<FestivalReviewRes>> getFestivalReviews(@PathVariable(name = "festival_id") int festivalId) {
        return new SuccessResponse<>(festivalService.getFestivalReview(festivalId));
    }

    @PatchMapping("/{festival_id}")
    public SuccessResponse<HttpStatus> editFestivalLike(HttpServletRequest request,
                                                        @PathVariable(name = "festival_id") int festivalId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(festivalService.updateFestivalLike(memberId, festivalId));
    }

    @PostMapping("/playlists")
    public SuccessResponse<HttpStatus> addYoutubePlaylist(HttpServletRequest request, @RequestBody FestivalSetlistReq festivalSetlistReq) {
        if (tokenProvider.isAdmin(request)) {
            int memberId = tokenProvider.getMemberIdByToken(request);
            festivalService.createYoutubePlaylist(memberId, festivalSetlistReq);
            return new SuccessResponse<>(HttpStatus.OK);
        }
       return new SuccessResponse<>(HttpStatus.ACCEPTED);
    }
}
