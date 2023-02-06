package com.toyproject.complaints.global.exception;

public class DeleteIpFailException extends RuntimeException{
    public DeleteIpFailException() {
        super(ErrorCode.FAIL_DELETE_IP_EXCEPTION.getErrorMessage());
    }

    public DeleteIpFailException(String message) {
        super(message);
    }
}

