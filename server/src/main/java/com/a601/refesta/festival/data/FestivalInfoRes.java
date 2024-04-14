package com.a601.refesta.festival.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class FestivalInfoRes {

    private int id;

    private String name;

    private LocalDate date;

    private String location;

    private String posterUrl;

    private int price;

    private boolean isEnded;

    private boolean isLiked;
}
