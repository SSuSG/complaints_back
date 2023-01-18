package com.toyproject.complaints.global.exception;

public class ExistPhoneNumberException extends RuntimeException {
    public ExistPhoneNumberException() {
        super(ErrorCode.EXIST_PHONE_NUMBER_EXCEPTION.getErrorMessage());
    }

    public ExistPhoneNumberException(String message) {
        super(message);
    }
}
