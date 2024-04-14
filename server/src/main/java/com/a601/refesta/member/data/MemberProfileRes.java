package com.a601.refesta.member.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileRes {

    private String nickname;
    private String profileUrl;
}
