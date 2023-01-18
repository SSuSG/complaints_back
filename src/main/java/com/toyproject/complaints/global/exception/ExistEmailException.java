package com.toyproject.complaints.global.exception;

public class ExistEmailException extends RuntimeException{
    public ExistEmailException() {
        super("이미 존재하는 이메일 입니다.");
    }

    public ExistEmailException(String message) {
        super(message);
    }
}
