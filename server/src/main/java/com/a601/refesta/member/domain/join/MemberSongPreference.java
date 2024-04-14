package com.a601.refesta.member.domain.join;

import com.a601.refesta.common.entity.BaseEntity;
import com.a601.refesta.member.domain.Member;
import com.a601.refesta.song.domain.Song;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSongPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer preference;

    public void updatePreference() {
        this.preference += 1;
    }

    public void plusPlayListPreference() {
        this.preference += 30;
    }
}
