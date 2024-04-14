package com.a601.refesta.common.jwt;

import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.login.data.MemberDetail;
import com.a601.refesta.login.data.MemberDetailAuthenticationToken;
import com.a601.refesta.login.data.OauthTokenRes;
import com.a601.refesta.member.domain.Member;
import com.a601.refesta.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    private static final String AUD = "https://j10a601.p.ssafy.io/";
    private static final String ISS = "https://j10a601.p.ssafy.io/";
    private final Key key;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private MemberRepository memberRepository;

    @Value("${spring.google.admin.googleId}")
    private String adminGoogleId;

    public TokenProvider(@Value("${spring.security.oauth2.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public OauthTokenRes generateTokenDto(Member member) {

        long now = (new Date()).getTime();
        //ACCESSTOKEN 생성
        String accessToken = Jwts.builder()
                .claim(AUTHORITIES_KEY, member.getRoles())
                .signWith(key, SignatureAlgorithm.HS512)
                .setAudience(AUD) //식별가능해야한다.
                .setSubject(String.valueOf(member.getGoogleId()))
                .setIssuer(ISS)
                .setIssuedAt(Timestamp.valueOf(now()))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .compact();

        //REFRESHTOKEN 생성
        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setAudience(AUD)
                .setSubject(String.valueOf(member.getGoogleId()))
                .setIssuer(ISS)
                .setIssuedAt(Timestamp.valueOf(now()))
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .compact();


        return OauthTokenRes.builder()
                .memberId(member.getId())
                .tokenType(BEARER_TYPE)
                .accessToken(accessToken)
                .expiresIn(ACCESS_TOKEN_EXPIRE_TIME - 1)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // 토큰을 검증하는 역할
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRE_ERROR);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            //권한 X |JWT가 올바르게 구성되지 않았을 때 | 수신한 JWT의 형식이 애플리케이션에서 원하는 형식과 맞지 않는 경우
            throw new CustomException(ErrorCode.ACCESS_TOKEN_ERROR);
        }
    }

    //토큰의 권한 검증
    public MemberDetailAuthenticationToken getAuthentication(String accessToken) {
        // 토큰 복호화 : JWT의 body
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                ((Collection<String>) claims.get(AUTHORITIES_KEY)).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        String googleId = claims.getSubject();
        Member member = memberRepository.findByGoogleId(googleId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND_ERROR);
        }
        MemberDetail memberDetail = MemberDetail.builder()
                .googleId(member.getGoogleId())
                .email(member.getEmail())
                .build();

        return new MemberDetailAuthenticationToken(authorities, memberDetail);

    }

    //복호화
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //검증된 refreshToken 복호화해서 Member 정보 가져오기
    public MemberDetail getUserByRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        String googleId = claims.getSubject();

        Member member = memberRepository.findByGoogleId(googleId);
        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND_ERROR);
        }
        return MemberDetail.builder()
                .googleId(member.getGoogleId())
                .email(member.getEmail())
                .build();
    }

    //토큰에서 memberId 꺼내기
    public Integer getMemberIdByToken(HttpServletRequest request) {
        //검증은 끝난 것이라 예외처리 하지 않음
        String accessToken = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        //복호화를 통해 googleID로 memberId꺼내기
        Claims claims = parseClaims(accessToken);
        String googleId = claims.getSubject();
        return memberRepository.findByGoogleId(googleId).getId();
    }

    public boolean isAdmin(HttpServletRequest request){
        String accessToken = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        //복호화를 통해 googleID로 memberId꺼내기
        Claims claims = parseClaims(accessToken);
        String googleId = claims.getSubject();
        return googleId.equals(adminGoogleId);
    }
}
