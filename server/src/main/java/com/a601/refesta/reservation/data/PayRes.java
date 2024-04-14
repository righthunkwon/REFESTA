package com.a601.refesta.reservation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayRes {

    private String tid;
    private String next_redirect_mobile_url;
    private String created_at;
}
