package com.toyproject.complaints.global.exception;

public class InValidPhoneNumberException extends RuntimeException{
    public InValidPhoneNumberException() {
        super("잘못된 휴대폰번호 형식 입니다.");
    }

    public InValidPhoneNumberException(String message) {
        super(message);
    }
}
