package com.a601.refesta.search.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SearchResultRes {

    private List<FestivalResult> festivalList;

    private List<ArtistResult> artistList;

    @Getter
    @AllArgsConstructor
    public static class FestivalResult {

        private int id;

        private String name;

        private String posterUrl;

        private boolean isEnded;
    }

    @Getter
    public static class ArtistResult {

        private int id;

        private String name;

        private String pictureUrl;

        private List<String> genre;

        public ArtistResult(int id, String name, String pictureUrl) {
            this.id = id;
            this.name = name;
            this.pictureUrl = pictureUrl;
        }

        public void setGenre(List<String> genreList) {
            this.genre = genreList;
        }
    }
}
