package com.a601.refesta.member.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberRole {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MEMBER("ROLE_MEMBER");

    private String value;
}
