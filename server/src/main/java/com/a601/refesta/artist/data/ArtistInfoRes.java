package com.a601.refesta.artist.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ArtistInfoRes {

    private int id;

    private String name;

    private String pictureUrl;

    private boolean isLiked;

    private List<String> genreList;

    private List<Performance> performanceList;

    @Getter
    @AllArgsConstructor
    public static class Performance {

        private int id;

        private String name;

        private String posterUrl;
    }
}
