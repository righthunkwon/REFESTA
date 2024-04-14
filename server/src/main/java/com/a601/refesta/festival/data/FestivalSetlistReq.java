package com.a601.refesta.festival.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FestivalSetlistReq {

    private String festivalName;
    private List<String> audioUrlList;
}
