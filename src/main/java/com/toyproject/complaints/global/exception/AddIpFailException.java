package com.toyproject.complaints.global.exception;

public class AddIpFailException extends RuntimeException{
    public AddIpFailException() {
        super(ErrorCode.FAIL_ADD_IP_BY_OVERSIZE_EXCEPTION.getErrorMessage());
    }

    public AddIpFailException(String message) {
        super(message);
    }
}

