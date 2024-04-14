package com.a601.refesta.recommendation.controller;

import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import com.a601.refesta.recommendation.data.ArtistRecommendationRes;
import com.a601.refesta.recommendation.data.EntireFestivalInfoRes;
import com.a601.refesta.recommendation.data.FestivalRecommendationRes;
import com.a601.refesta.recommendation.service.RecommendationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final TokenProvider tokenProvider;

    @GetMapping("/festivals")
    public SuccessResponse<FestivalRecommendationRes> getFestivalRecommendation(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.getFestivalRecommendation(memberId));
    }

    @GetMapping("/artists")
    public SuccessResponse<ArtistRecommendationRes> getArtistRecommendation(HttpServletRequest request,
                                                                            @RequestParam(name = "page") int pageNo) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.getArtistRecommendation(memberId, pageNo));
    }

    @GetMapping("/scheduled-festivals")
    public SuccessResponse<List<EntireFestivalInfoRes>> getEntireScheduledFestival(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.getEntireScheduledFestival(memberId));
    }

    @GetMapping("/ended-festivals")
    public SuccessResponse<List<EntireFestivalInfoRes>> getEntireEndedFestival(HttpServletRequest request) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.getEntireEndedFestival(memberId));
    }

    @PatchMapping("/festivals/{festival_id}")
    public SuccessResponse<HttpStatus> editFestivalPreference(HttpServletRequest request,
                          @PathVariable(name = "festival_id") int festivalId, @RequestParam(name = "point") int point) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.updateFestivalPreference(memberId, festivalId, point));
    }

    @PatchMapping("/songs/{song_id}")
    public SuccessResponse<HttpStatus> editSongPreference(HttpServletRequest request,
                                                          @PathVariable(name = "song_id") int songId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.updateSongPreference(memberId, songId));
    }

    @PatchMapping("/artists/{artist_id}")
    public SuccessResponse<HttpStatus> editArtistPreference(HttpServletRequest request,
                              @PathVariable(name = "artist_id") int artistId, @RequestParam(name = "point") int point) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(recommendationService.updateArtistPreference(memberId, artistId, point));
    }
}
