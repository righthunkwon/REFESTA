package com.a601.refesta.search.data;

import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AutoCompleteRes {

    private List<SearchWord> festivalWordList;

    private List<SearchWord> artistWordList;

    @Getter
    @AllArgsConstructor
    public static class SearchWord {

        private int id;

        private String name;
    }
}
