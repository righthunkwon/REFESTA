package com.a601.refesta.common.exception;

import com.a601.refesta.common.response.FailResponse;
import com.querydsl.core.NonUniqueResultException;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // CustomException이 발생했을 때 처리하는 메서드
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<FailResponse<Map<String, String>>> handle(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                FailResponse.of(errorCode.getCode(), errorCode.getMessage()),
                HttpStatusCode.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailResponse<Map<String, String>>> handleException(Exception e) {
        return new ResponseEntity<>(
                FailResponse.of("SEV001", "서버 동작 중 에러가 발생했습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // MissingServletRequestParameterException이 발생했을 때 처리하는 메서드
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<FailResponse<Map<String, String>>> handle(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(
                FailResponse.of("PAR001", "Param 형식 잘못됨"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<FailResponse<Map<String, String>>> handle(NonUniqueResultException ex) {
        return new ResponseEntity<>(
                FailResponse.of("RES001", "결과가 둘 이상입니다."),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<FailResponse<Map<String, String>>> handle(NoResultException ex) {
        return new ResponseEntity<>(
                FailResponse.of("RES002", "결과가 존재하지 않습니다."),
                HttpStatus.NOT_FOUND);
    }
}
