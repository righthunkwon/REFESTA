package com.a601.refesta.recommendation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class EntireFestivalInfoRes {

    private int id;

    private String name;

    private LocalDate date;

    private String location;

    private String posterUrl;

    private String lineup;
}
