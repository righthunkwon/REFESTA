package com.a601.refesta.review.controller;

import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import com.a601.refesta.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public SuccessResponse<Integer> addReview(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                              @RequestParam(value = "contents", required = false) String contents,
                                              @RequestParam("festivalId") Integer festivalId) {

        int memberId = tokenProvider.getMemberIdByToken(request);
        reviewService.createReview(memberId, festivalId, contents, file);
        return new SuccessResponse<>(HttpStatus.SC_CREATED);
    }

    @DeleteMapping("/{review_id}")
    public SuccessResponse<Integer> deleteReview(HttpServletRequest request, @PathVariable(name = "review_id") Integer reviewId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        reviewService.deleteReview(memberId, reviewId);
        return new SuccessResponse<>(HttpStatus.SC_OK);
    }
}
