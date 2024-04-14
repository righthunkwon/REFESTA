package com.a601.refesta.login.service;

import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.login.data.*;
import com.a601.refesta.login.repository.RefreshTokenRepository;
import com.a601.refesta.member.data.MemberRole;
import com.a601.refesta.member.domain.Member;
import com.a601.refesta.member.repository.MemberRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GoogleAccessTokenService googleAccessTokenService;

    private final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.provider.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.refesta.front.url}")
    private String REFESTA_URL;

    private final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    @Value("${spring.security.oauth2.provider.google.client-secret}")
    private String CLIENT_SECRET;

    public GoogleOAuthTokenRes getTokenbyCode(String code) {

        // POST 방식으로 key=value 데이터를 요청 (구글 쪽으로)
        // 이 때 필요한 라이브러리가 RestTemplate, 얘를 쓰면 http 요청을 편하게 할 수 있다.
        RestTemplate rt = new RestTemplate();

        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 만들어 같이 보내줘야 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("redirect_uri", REFESTA_URL + "/google-login");

        // 요청하기 위해 헤더(Header)와 데이터(Body)를 합친다.
        // googleTokenRequest는 데이터(Body)와 헤더(Header)를 Entity가 된다.
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), GoogleOAuthTokenRes.class);

    }

    public GoogleUserInfoRes getUserInfoByToken(String accessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://www.googleapis.com/userinfo/v2/me?access_token=" + accessToken,
                HttpMethod.GET,
                req,
                String.class
        );

        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), GoogleUserInfoRes.class);
    }

    public OauthTokenRes getAccessTokenJsonData(String code) {
        //코드로 토큰받기
        GoogleOAuthTokenRes oauthTokenData = getTokenbyCode(code);

        //토큰으로 사용자 정보 받기
        GoogleUserInfoRes googleUserInfoRes = getUserInfoByToken(oauthTokenData.getAccess_token());

        //받아온 사용자 정보(로그인/회원가입한 유저)로 우리 토큰 만들기
        return generateTokenbyUserInfo(googleUserInfoRes, oauthTokenData.getAccess_token());
    }

    private OauthTokenRes generateTokenbyUserInfo(GoogleUserInfoRes googleUserInfoRes, String googleAccessToken) {
        boolean signUp = false;
        if (memberRepository.findByGoogleId(googleUserInfoRes.getId()) == null) { //우리 회원이 아니면
            Member newMember = Member.builder()
                    .googleId(googleUserInfoRes.getId())
                    .email(googleUserInfoRes.getEmail())
                    .nickname(googleUserInfoRes.getName())
                    .profileUrl(googleUserInfoRes.getPicture())
                    .role(MemberRole.ROLE_MEMBER)
                    .build();
            memberRepository.save(newMember);
            signUp = true;


        }
        //회원가입: 레디스에 구글 accessToken 저장 test 위해서 밖으로 빼놓음
        googleAccessTokenService.saveTokenInfo(
                memberRepository.findByGoogleId(googleUserInfoRes.getId()).getId(), googleAccessToken
        );

        Member member = memberRepository.findByGoogleId(googleUserInfoRes.getId());
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND_ERROR);
        }
        OauthTokenRes oauthTokenRes = tokenProvider.generateTokenDto(member);

        //refreshToken redis에 저장
        refreshTokenService.saveTokenInfo(
                member.getId(), oauthTokenRes.getRefreshToken(),
                LocalDateTime.now().plusSeconds(oauthTokenRes.getRefreshTokenExpiresIn()),
                false
        );

        if (signUp) oauthTokenRes.isSignUp(true);

        return oauthTokenRes;
    }

    public OauthTokenRes regenerateToken(String refreshTokenReq) {
        //Refresh Token 일치 확인
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenReq).orElseThrow(() ->
                new CustomException(ErrorCode.REFRESH_TOKEN_VALIDATION_ERROR));

        //Refresh Token 만료 여부
        if (!refreshToken.isValid(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_VALIDATION_ERROR);
        }

        //tokenProvider에서 refreshToken으로 member정보 받기
        MemberDetail memberDetail = tokenProvider.getUserByRefreshToken(refreshTokenReq);

        Member member = memberRepository.findByGoogleId(memberDetail.getGoogleId());
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND_ERROR);
        }

        //Member 정보로 토큰 재발급
        OauthTokenRes oauthTokenRes = tokenProvider.generateTokenDto(member);

        //redis에 저장
        refreshTokenService.saveTokenInfo(
                member.getId(), oauthTokenRes.getRefreshToken(),
                LocalDateTime.now().plusSeconds(oauthTokenRes.getRefreshTokenExpiresIn()),
                false
        );


        return oauthTokenRes;
    }
}
