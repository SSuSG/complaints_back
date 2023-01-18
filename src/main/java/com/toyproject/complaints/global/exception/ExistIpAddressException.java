package com.toyproject.complaints.global.exception;

public class ExistIpAddressException extends RuntimeException{
    public ExistIpAddressException() {
        super(ErrorCode.EXIST_IP_EXCEPTION.getErrorMessage());
    }

    public ExistIpAddressException(String message) {
        super(message);
    }
}
