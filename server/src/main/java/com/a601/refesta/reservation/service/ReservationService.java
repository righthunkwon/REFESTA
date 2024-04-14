package com.a601.refesta.reservation.service;

import com.a601.refesta.common.exception.CustomException;
import com.a601.refesta.common.exception.ErrorCode;
import com.a601.refesta.festival.domain.Festival;
import com.a601.refesta.festival.repository.FestivalRepository;
import com.a601.refesta.member.repository.MemberRepository;
import com.a601.refesta.reservation.data.ApproveRes;
import com.a601.refesta.reservation.data.PayRes;
import com.a601.refesta.reservation.data.ReservationReq;
import com.a601.refesta.reservation.data.ReservationRes;
import com.a601.refesta.reservation.domain.Reservation;
import com.a601.refesta.reservation.repository.ReservationRepository;
import com.google.gson.Gson;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.a601.refesta.festival.domain.QFestival.festival;
import static com.a601.refesta.reservation.domain.QReservation.reservation;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final FestivalRepository festivalRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Value("${pay.admin-key}")
    private String adminKey;

    @Value("${spring.refesta.front.url}")
    private String REFESTA_URL;

    //결제창 띄우기 위해 요청하는 것
    public String getKakaoPayUrl(int memberId, ReservationReq reservationReq) {

        Festival festival = festivalRepository.findById(reservationReq.getFestivalId()).orElseThrow();
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = "SECRET_KEY " + adminKey;
        headers.set("Authorization", auth);

        Map<String, Object> req = new HashMap<>();
        req.put("cid", "TC0ONETIME");
        req.put("partner_order_id", "refesta" + memberId);
        req.put("partner_user_id", "ReFesta");
        req.put("item_name", festival.getName());
        req.put("quantity", reservationReq.getCount());
        req.put("total_amount", festival.getPrice() * reservationReq.getCount());
        req.put("tax_free_amount", 0);
        req.put("approval_url", REFESTA_URL + "/reservation/approve");
        req.put("cancel_url", REFESTA_URL + "/reservation/cancel");
        req.put("fail_url", REFESTA_URL + "/reservation/fail");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(req, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        Gson gson = new Gson();

        PayRes payRes = gson.fromJson(response.getBody(), PayRes.class);

        Reservation reservation = Reservation.builder()
                .member(memberRepository.findById(memberId).orElseThrow())
                .festival(festival)
                .count(reservationReq.getCount())
                .tid(payRes.getTid())
                .status("READY")
                .build();

        reservationRepository.save(reservation);

        return payRes.getNext_redirect_mobile_url();
    }

    //카카오에 결제 승인 요청
    public int getKaKaoPayApprove(Integer memberId, String pgToken) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = "SECRET_KEY " + adminKey;
        headers.set("Authorization", auth);

        Map<String, Object> req = new HashMap<>();
        req.put("cid", "TC0ONETIME");
        req.put("tid", getTid(memberId));
        req.put("partner_order_id", "refesta" + memberId);
        req.put("partner_user_id", "ReFesta");
        req.put("pg_token", pgToken);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(req, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        Gson gson = new Gson();

        ApproveRes approveRes = gson.fromJson(response.getBody(), ApproveRes.class);
        if (approveRes.getError_message() != null || approveRes.getTid() == null) {
            throw new CustomException(ErrorCode.KAKAOPAY_FAILED_ERROR);
        }
        //결제상태 변경 : 준비 -> 성공
        Reservation reservation = reservationRepository.findByTid(approveRes.getTid()).orElseThrow();
        reservation.statusSuccess();
        reservationRepository.save(reservation);
        return reservation.getId();

    }

    /**
     * 예매 TID
     *
     * @param memberId
     * @return TID
     */
    public String getTid(int memberId) {

        return jpaQueryFactory.select(reservation.tid)
                .from(reservation)
                .where(reservation.member.id.eq(memberId), reservation.status.eq("READY"))
                .orderBy(reservation.createdDate.desc()).limit(1)
                .fetchOne();
    }

    /**
     * 예매상세내역
     *
     * @param memberId
     * @param reservationId
     * @return 예매상세정보- 페스티벌(포스터, 이름, 날짜, 장소), 예약(장수, 총 가격)
     */
    public ReservationRes getReservation(int memberId, Integer reservationId) {

        //예약자와 현재 회원이 다를 때(내 예매내역이 아닐 경우)
        if (reservationRepository.findById(reservationId).orElseThrow().getMember().getId() != memberId) {
            throw new CustomException(ErrorCode.RESERVATION_MEMBER_NOT_EQUAL);
        }

        //장수 * 가격
        NumberExpression<Integer> totalPrice = reservation.count.multiply(festival.price);

        ReservationRes reservationRes =
                jpaQueryFactory.select(Projections.constructor(ReservationRes.class,
                                festival.posterUrl, festival.name, festival.festivalDate,
                                festival.location, reservation.count, totalPrice, reservation.createdDate))
                        .from(festival)
                        .innerJoin(reservation).on(reservation.festival.id.eq(festival.id))
                        .where(reservation.id.eq(reservationId), reservation.status.eq("SUCCESS"))
                        .fetchOne();

        //결제 성공한 예매 내역이 없을 경우
        if (reservationRes == null) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOUND_ERROR);
        }

        return reservationRes;
    }
}
