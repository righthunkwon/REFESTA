package com.a601.refesta.login.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetail {

    private String googleId;
    private String email;
}
