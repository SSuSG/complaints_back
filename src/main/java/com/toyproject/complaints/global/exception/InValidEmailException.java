package com.toyproject.complaints.global.exception;

public class InValidEmailException extends RuntimeException{

    public InValidEmailException() {
        super("잘못된 이메일 형식 입니다.");
    }

    public InValidEmailException(String message) {
        super(message);
    }
}
