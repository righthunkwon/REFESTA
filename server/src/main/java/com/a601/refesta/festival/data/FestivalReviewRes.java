package com.a601.refesta.festival.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FestivalReviewRes {

    private String writer;

    private String profileUrl;

    private String attachmentUrl;

    private String mediaType;

    private String contents;
}
