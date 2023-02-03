package com.toyproject.complaints.global.exception;

public class LoginFailException extends RuntimeException{

    public LoginFailException() {
        super(ErrorCode.FAIL_LOGIN_EXCEPTION.getErrorMessage());
    }

    public LoginFailException(String message) {
        super(message);
    }
}
