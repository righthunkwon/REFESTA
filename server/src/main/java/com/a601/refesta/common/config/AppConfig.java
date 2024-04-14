package com.a601.refesta.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public Map<Integer, List<String>> genreMap() {
        Map<Integer, List<String>> genreMap = new HashMap<>();

        genreMap.put(1, new ArrayList<>(List.of("발라드", "ballade")));
        genreMap.put(2, new ArrayList<>(List.of("댄스", "dance")));
        genreMap.put(3, new ArrayList<>(List.of("랩/힙합", "랩", "힙합", "rap/hiphop", "rap", "hiphop")));
        genreMap.put(4, new ArrayList<>(List.of("R&B/Soul", "알앤비", "소울", "R&B", "Soul")));
        genreMap.put(5, new ArrayList<>(List.of("인디음악", "indie")));
        genreMap.put(6, new ArrayList<>(List.of("록/메탈", "록", "메탈", "rock", "metal")));
        genreMap.put(7, new ArrayList<>(List.of("포크/블루스", "포크", "블루스", "folk", "blues")));
        genreMap.put(8, new ArrayList<>(List.of("일렉트로니카", "electronica")));
        genreMap.put(9, new ArrayList<>(List.of("재즈", "jazz")));

        return genreMap;
    }

    @Bean
    public List<String> consonantsList() {
        List<String> consonantsList = new ArrayList<>();

        consonantsList.add("ㄱ");
        consonantsList.add("ㄴ");
        consonantsList.add("ㄷ");
        consonantsList.add("ㄹ");
        consonantsList.add("ㅁ");
        consonantsList.add("ㅂ");
        consonantsList.add("ㅅ");
        consonantsList.add("ㅇ");
        consonantsList.add("ㅈ");
        consonantsList.add("ㅊ");
        consonantsList.add("ㅋ");
        consonantsList.add("ㅌ");
        consonantsList.add("ㅍ");
        consonantsList.add("ㅎ");

        return consonantsList;
    }


    @Bean
    public Map<Integer, String> syllableMap() {
        Map<Integer, String> syllableMap = new HashMap<>();

        syllableMap.put(0, "가");
        syllableMap.put(1, "나");
        syllableMap.put(2, "다");
        syllableMap.put(3, "라");
        syllableMap.put(4, "마");
        syllableMap.put(5, "바");
        syllableMap.put(6, "사");
        syllableMap.put(7, "아");
        syllableMap.put(8, "자");
        syllableMap.put(9, "차");
        syllableMap.put(10, "카");
        syllableMap.put(11, "타");
        syllableMap.put(12, "파");
        syllableMap.put(13, "하");
        syllableMap.put(14, "힣");

        return syllableMap;
    }
}
