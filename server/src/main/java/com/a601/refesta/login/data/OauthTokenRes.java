package com.a601.refesta.login.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OauthTokenRes {

    private Integer memberId;

    private String tokenType;

    private String accessToken;

    private Integer expiresIn;

    private String refreshToken;

    private Integer refreshTokenExpiresIn;

    private boolean signUp;

    public void isSignUp(boolean signUp) {
        this.signUp = signUp;
    }
}
