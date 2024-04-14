package com.a601.refesta.reservation.controller;


import com.a601.refesta.common.jwt.TokenProvider;
import com.a601.refesta.common.response.SuccessResponse;
import com.a601.refesta.reservation.data.ReservationReq;
import com.a601.refesta.reservation.data.ReservationRes;
import com.a601.refesta.reservation.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;


@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final TokenProvider tokenProvider;


    @Value("${spring.refesta.front.url}")
    private String REFESTA_URL;

    @PostMapping
    public SuccessResponse<Map<String, String>> getKakaoPayUrl(HttpServletRequest request, @RequestBody ReservationReq reservationReq) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        Map<String, String> data = new TreeMap<>();
        data.put("redirect_url", reservationService.getKakaoPayUrl(memberId, reservationReq));
        return new SuccessResponse<>(data);
    }

    @PostMapping("/success")
    public SuccessResponse<Map<String, Integer>> getKaKaoPayApprove(HttpServletRequest request,
                                                                    @RequestBody String pgToken) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        Map<String, Integer> data = new TreeMap<>();
        data.put("reservation_id", reservationService.getKaKaoPayApprove(memberId, pgToken));
        return new SuccessResponse<>(data);
    }

    @GetMapping("/{reservation_id}")
    public SuccessResponse<ReservationRes> getReservation(HttpServletRequest request,
                                                          @PathVariable("reservation_id") Integer reservationId) {
        int memberId = tokenProvider.getMemberIdByToken(request);
        ReservationRes reservationRes = reservationService.getReservation(memberId, reservationId);
        return new SuccessResponse<>(reservationRes);
    }

}
