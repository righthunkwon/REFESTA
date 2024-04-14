package com.a601.refesta.login.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "googleAccessTokenRedis", timeToLive = 60 * 60) //레디스 데이터 유효기간 :1시간 (구글 accessToken 1시간이라서)
public class GoogleAccessToken {

    @Id
    private String id;

    private String googleAccessToken;
}
