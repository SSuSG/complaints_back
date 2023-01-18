package com.toyproject.complaints.global.exception;

public class ExistPhoeNumberException extends RuntimeException {
    public ExistPhoeNumberException() {
        super("이미 존재하는 핸드폰 번호입니다.");
    }

    public ExistPhoeNumberException(String message) {
        super(message);
    }
}
