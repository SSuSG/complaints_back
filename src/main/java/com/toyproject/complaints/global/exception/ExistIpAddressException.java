package com.toyproject.complaints.global.exception;

public class ExistIpAddressException extends RuntimeException{
    public ExistIpAddressException() {
        super("이미 존재하는 아이피 입니다.");
    }

    public ExistIpAddressException(String message) {
        super(message);
    }
}
