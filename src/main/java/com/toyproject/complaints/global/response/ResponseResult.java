package com.toyproject.complaints.global.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
public class ResponseResult {

    int statusCode;
    String messages;
    String developerMessage;
    LocalDateTime timestamp;

    public static final ResponseResult successResponse =
            ResponseResult.builder()
            .statusCode(HttpStatus.OK.value())
            .messages("성공 :)")
            .developerMessage("성공하였습니다.")
            .timestamp(LocalDateTime.now()).build();

    public static final ResponseResult failResponse =
            ResponseResult.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .messages("실패 :(")
            .developerMessage("실패하였습니다.")
            .timestamp(LocalDateTime.now()).build();

    public static final ResponseResult LockResponse =
            ResponseResult.builder()
            .statusCode(HttpStatus.LOCKED.value())
            .messages("잠금 :(")
            .developerMessage("잠금 계정입니다.")
            .timestamp(LocalDateTime.now()).build();

    public static final ResponseResult exceptionResponse(Exception e , int errorStatusCode){
        return ResponseResult.builder()
                .statusCode(errorStatusCode)
                .messages("에러발생 :(")
                .developerMessage(e.getMessage())
                .timestamp(LocalDateTime.now()).build();
    }

}
