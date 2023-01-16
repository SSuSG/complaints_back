package com.toyproject.complaints.global.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
