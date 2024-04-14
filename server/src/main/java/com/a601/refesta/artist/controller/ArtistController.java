package com.a601.refesta.artist.controller;

import com.a601.refesta.artist.data.ArtistInfoRes;
import com.a601.refesta.artist.service.ArtistService;
import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final TokenProvider tokenProvider;

    @GetMapping("/{artist_id}")
    public SuccessResponse<ArtistInfoRes> getArtistInfo(HttpServletRequest request,
                                                        @PathVariable(name = "artist_id") int artistId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(artistService.getArtistInfo(memberId, artistId));
    }

    @PatchMapping("/{artist_id}")
    public SuccessResponse<HttpStatus> editArtistLike(HttpServletRequest request,
                                                      @PathVariable(name = "artist_id") int artistId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(artistService.updateArtistLike(memberId, artistId));
    }
}
