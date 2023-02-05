package com.toyproject.complaints.global.exception;

public class InValidIpException extends RuntimeException{
    public InValidIpException() {
        super("잘못된 아이피 형식 입니다.");
    }

    public InValidIpException(String message) {
        super(message);
    }
}
