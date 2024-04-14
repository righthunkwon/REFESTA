package com.a601.refesta.search.controller;

import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import com.a601.refesta.search.data.AutoCompleteRes;
import com.a601.refesta.search.data.SearchResultRes;
import com.a601.refesta.search.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public SuccessResponse<AutoCompleteRes> getAutoComplete(HttpServletRequest request,
                                                            @RequestParam("word") String inputWord) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(searchService.getAutoComplete(memberId, inputWord));
    }

    @GetMapping("/results")
    public SuccessResponse<SearchResultRes> getSearchResult(HttpServletRequest request,
                                                            @RequestParam("word") String searchWord) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        return new SuccessResponse<>(searchService.getSearchResult(memberId, searchWord));
    }
}
