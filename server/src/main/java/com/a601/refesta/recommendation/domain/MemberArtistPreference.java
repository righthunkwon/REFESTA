package com.a601.refesta.recommendation.domain;

import com.a601.refesta.artist.domain.Artist;
import com.a601.refesta.common.entity.BaseEntity;
import com.a601.refesta.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberArtistPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Artist_id")
    private Artist artist;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer preference;

    public void updatePreference(int point) {
        this.preference += point;
    }
}
