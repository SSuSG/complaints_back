package com.toyproject.complaints.global.exception;

public class ExistEmailException extends RuntimeException{
    public ExistEmailException() {
        super(ErrorCode.EXIST_EMAIL_EXCEPTION.getErrorMessage());
    }

    public ExistEmailException(String message) {
        super(message);
    }
}
