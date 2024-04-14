package com.a601.refesta.reservation.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRes {

    private String posterUrl;
    private String name;
    private LocalDate festivalDate;
    private String location;
    private int count;
    private int price;
    private LocalDateTime paymentDate;
}
