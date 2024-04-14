package com.a601.refesta.reservation.domain;

import com.a601.refesta.common.entity.BaseEntity;
import com.a601.refesta.festival.domain.Festival;
import com.a601.refesta.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private String tid;

    @Column(nullable = false)
    private String status;

    public void statusSuccess() {
        this.status = "SUCCESS";
    }

    public void statusFail() {
        this.status = "FAIL";
    }
}
