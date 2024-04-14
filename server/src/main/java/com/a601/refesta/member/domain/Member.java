package com.a601.refesta.member.domain;

import com.a601.refesta.common.entity.BaseEntity;
import com.a601.refesta.member.data.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String googleId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, length = 2100)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role = MemberRole.ROLE_MEMBER;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Collection<String> getRoles() {
        Collection<String> roles = new ArrayList<>();
        roles.add(role.getValue());
        return roles;
    }
}
