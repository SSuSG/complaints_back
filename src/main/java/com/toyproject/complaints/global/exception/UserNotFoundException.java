package com.toyproject.complaints.global.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super(ErrorCode.NOT_EXIST_USER_EXCEPTION.getErrorMessage());
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
