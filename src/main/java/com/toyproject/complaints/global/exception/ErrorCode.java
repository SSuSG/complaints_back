package com.toyproject.complaints.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {


    INVALID_ACCESS_EXCEPTION(417,"일반회원은 접근이 불가능합니다."),
    INVALID_EMAIL_EXCEPTION(417,"잘못된 이메일 형식입니다."),
    INVALID_IP_EXCEPTION(417,"잘못된 IP 형식입니다."),
    INVALID_PHONE_NUMBER_EXCEPTION(417,"잘못된 핸드폰번호 형식입니다."),
    EXIST_EMAIL_EXCEPTION(404,"이미 존재하는 이메일 입니다."),
    EXIST_IP_EXCEPTION(404,"이미 존재하는 IP 입니다."),
    EXIST_PHONE_NUMBER_EXCEPTION(404,"이미 존재하는 핸드폰번호 입니다."),
    NOT_EXIST_USER_EXCEPTION(404,"존재하지 않는 회원입니다."),
    SERVER_EXCEPTION(500,"서버에서 예측하지 못한 에러가 발생했습니다."),
    N0_CONNECTION_EXCEPTION(410,"연결시간이 초과되었습니다."),
    NO_LOGIN_EXCEPTION(400,"로그인상태가 아닙니다. 로그인 해주세요."),
    PARSE_EXCEPTION(400,"날짜형식이 올바르지 않습니다.");

    private int errorCode;
    private String errorMessage;

    ErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
