package com.toyproject.complaints.global.exception;

import com.toyproject.complaints.global.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.toyproject.complaints")
public class ExceptionController {

    @ExceptionHandler(ExistEmailException.class)
    public ResponseResult ExistEmailException(ExistEmailException e) {
        log.info("Error : {}",e.getClass());
        log.info("Error Message : {}",e.getMessage());
        return ResponseResult.exceptionResponse(e,ErrorCode.EXIST_EMAIL_EXCEPTION.getErrorCode());
    }

    @ExceptionHandler(ExistPhoneNumberException.class)
    public ResponseResult ExistPhoneNumberException(ExistPhoneNumberException e) {
        log.info("Error : {}",e.getClass());
        log.info("Error Message : {}",e.getMessage());
        return ResponseResult.exceptionResponse(e,ErrorCode.EXIST_PHONE_NUMBER_EXCEPTION.getErrorCode());
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseResult SignInFailedException(LoginFailException e) {

        log.info("Error : {}",e.getClass());
        log.info("Error Message : {}",e.getMessage());
        return ResponseResult.exceptionResponse(e,ErrorCode.FAIL_LOGIN_EXCEPTION.getErrorCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseResult UserNotFoundException(UserNotFoundException e)
    {
        log.info("Error : {}",e.getClass());
        log.info("Error Message : {}",e.getMessage());
        return ResponseResult.exceptionResponse(e,ErrorCode.NOT_EXIST_USER_EXCEPTION.getErrorCode());
    }

    @ExceptionHandler({MailException.class , MessagingException.class ,IllegalArgumentException.class })
    public ResponseResult MailException(Exception e) {

        log.info("Error : {}",e.getClass());
        log.info("Error Message : {}",e.getMessage());
        return ResponseResult.exceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler({InValidEmailException.class , InValidPhoneNumberException.class , InValidIpException.class})
    public ResponseResult InvalidException(Exception e)
    {
        log.info("Error : {}",e.getClass());
        log.info("Error Message : {}",e.getMessage());
        return ResponseResult.exceptionResponse(e, HttpStatus.BAD_REQUEST.value());
    }
}
