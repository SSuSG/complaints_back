package com.toyproject.complaints.global.exception;

public class InValidAccessException extends RuntimeException{
    public InValidAccessException() {
        super(ErrorCode.INVALID_ACCESS_EXCEPTION.getErrorMessage());
    }

    public InValidAccessException(String message) {
        super(message);
    }
}
