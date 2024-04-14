package com.a601.refesta.login.service;

import com.a601.refesta.login.repository.RefreshTokenRepository;
import com.a601.refesta.login.data.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(Integer memberId, String refreshToken, LocalDateTime expireDate, Boolean isExpired) {
        refreshTokenRepository.save(new RefreshToken(String.valueOf(memberId), refreshToken, expireDate, isExpired));
    }
    //로그아웃 처리 추가
}
