package com.a601.refesta.member.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeArtistRes {

    private int artistId;
    private String pictureUrl;
    private String name;
    private boolean isLike;
}
