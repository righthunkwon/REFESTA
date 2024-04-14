package com.a601.refesta.member.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeFestivalRes {

    private int festivalId;
    private String posterUrl;
    private String name;
    private boolean isLike;
    private boolean isEnded;
}
