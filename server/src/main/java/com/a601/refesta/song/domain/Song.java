package com.a601.refesta.song.domain;

import com.a601.refesta.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Song extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2100)
    private String audioUrl;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false, length = 2100)
    private String imageUrl;
}
