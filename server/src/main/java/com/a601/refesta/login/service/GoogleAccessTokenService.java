package com.a601.refesta.login.service;

import com.a601.refesta.login.data.GoogleAccessToken;
import com.a601.refesta.login.repository.GoogleAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoogleAccessTokenService {

    private final GoogleAccessTokenRepository googleAccessTokenRepository;

    @Transactional
    public void saveTokenInfo(Integer memberId, String googleAccessToken) {
        googleAccessTokenRepository.save(new GoogleAccessToken(String.valueOf(memberId), googleAccessToken));
    }
}
