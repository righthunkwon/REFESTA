package com.a601.refesta.review.domain;

import com.a601.refesta.common.entity.BaseEntity;
import com.a601.refesta.festival.domain.Festival;
import com.a601.refesta.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String attachmentUrl;

    @Column(nullable = false)
    private String mediaType;

    private String contents;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    public void delete() {
        this.isDeleted = true;
    }

}
