package com.toyproject.complaints.global.exception;

import lombok.Getter;


/**
 *  ErrorCode는 임의로 지정하였음
 */
@Getter
public enum ErrorCode {

    PARSE_EXCEPTION(400,"날짜형식이 올바르지 않습니다."),
    NO_LOGIN_EXCEPTION(401,"로그인상태가 아닙니다. 로그인 해주세요."),
    INVALID_ACCESS_EXCEPTION(403,"일반회원은 접근이 불가능합니다."),
    INVALID_EMAIL_EXCEPTION(406,"잘못된 이메일 형식입니다."),
    INVALID_IP_EXCEPTION(406,"잘못된 IP 형식입니다."),
    INVALID_PHONE_NUMBER_EXCEPTION(406,"잘못된 핸드폰번호 형식입니다."),
    FAIL_LOGIN_EXCEPTION(406,"이메일 또는 비밀번호 또는 등록된 ip가 일치하지 않습니다."),
    NOT_EXIST_USER_EXCEPTION(408,"존재하지 않는 회원입니다."),
    EXIST_EMAIL_EXCEPTION(409,"이미 존재하는 이메일 입니다."),
    EXIST_IP_EXCEPTION(409,"이미 존재하는 IP 입니다."),
    EXIST_PHONE_NUMBER_EXCEPTION(409,"이미 존재하는 핸드폰번호 입니다."),
    FAIL_ADD_IP_BY_OVERSIZE_EXCEPTION(410,"등록할 수 있는 IP의 수는 3개입니다."),
    FAIL_DELETE_IP_EXCEPTION(410,"삭제할 수 있는 IP가 존재하지 않습니다."),
    N0_CONNECTION_EXCEPTION(411,"연결시간이 초과되었습니다."),
    SERVER_EXCEPTION(500,"서버에서 예측하지 못한 에러가 발생했습니다.");

    private int errorCode;
    private String errorMessage;

    ErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
