package com.a601.refesta.festival.domain;

import com.a601.refesta.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate festivalDate;

    @Column(nullable = false, length = 2100)
    private String posterUrl;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isEnded;
}
