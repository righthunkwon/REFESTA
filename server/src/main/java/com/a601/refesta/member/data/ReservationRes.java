package com.a601.refesta.member.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRes {

    private int reservationId;
    private String name;
    private LocalDate festivalDate;
    private String location;
}
