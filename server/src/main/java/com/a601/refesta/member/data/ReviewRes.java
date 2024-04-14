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
public class ReviewRes {

    private int reviewId;
    private String name;
    private LocalDate festivalDate;
    private String location;
    private String contents;
    private String attachmentUrl;
    private String type;
}
