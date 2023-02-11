package com.toyproject.complaints.global.exception;

public class FailReadUserException extends RuntimeException{
    public FailReadUserException() {
        super(ErrorCode.FAIL_READ_USER_EXCEPTION.getErrorMessage());
    }

    public FailReadUserException(String message) {
        super(message);
    }
}
