package com.a601.refesta.festival.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class FestivalSetlistRes {

    private List<ArtistInfo> lineupList;

    private Map<Integer, List<SongInfo>> songInfoMap;

    @Getter
    @AllArgsConstructor
    public static class ArtistInfo {

        private int id;

        private String name;

        private String pictureUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class SongInfo {

        private int id;

        private String title;

        private String audioUrl;

        private String imageUrl;

        private String singer;
    }
}
