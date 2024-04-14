package com.a601.refesta.recommendation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ArtistRecommendationRes {

    private List<ArtistInfo> artistInfoList;

    @Getter
    @AllArgsConstructor
    public static class ArtistInfo {

        private int id;

        private String name;

        private String pictureUrl;
    }
}
