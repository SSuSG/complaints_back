package com.toyproject.complaints.domain.user.controller;

import com.toyproject.complaints.domain.user.dto.request.CertificateAuthenticationKeyRequestDto;
import com.toyproject.complaints.domain.user.dto.request.CreateUserAccountRequestDto;
import com.toyproject.complaints.domain.user.dto.request.SendAuthenticationKeyRequestDto;
import com.toyproject.complaints.domain.user.service.AccountService;
import com.toyproject.complaints.domain.user.service.SmsService;
import com.toyproject.complaints.global.exception.UserNotFoundException;
import com.toyproject.complaints.global.response.ResponseResult;
import com.toyproject.complaints.global.response.SingleResponseResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class AccountController {

    private final AccountService accountService;
    private final SmsService smsService;

    /**
     * @throws MessagingException   -> 메일 발송에 실패할 경우
     */
    @ApiOperation(value = "사용자 계정 생성" , notes = "슈퍼관리자가 사용자의 계정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "계정생성 성공"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음"),
            @ApiResponse(code = 409, message = "중복이메일 존재"),
            @ApiResponse(code = 500, message = "이메일 전송 실패"),
    })
    @PostMapping("/accounts")
    public ResponseResult createUserAccount(@Valid @RequestBody CreateUserAccountRequestDto createUserAccountRequestDto) throws MessagingException {
        log.info("AccountController_createUserAccount || 관리자가 새로운 계정 생성");
        if(accountService.createUserAccount(createUserAccountRequestDto) != null)
            return ResponseResult.successResponse;
        else
            return ResponseResult.failResponse;
    }

    @ApiOperation(value = "계정잠금해제 인증번호 요청" , notes = "사용자가 잠금된 계정을 해제하기위해 인증번호를 요청")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "인증번호 요청 성공"),
            @ApiResponse(code = 408, message = "인증번호 요청한 사용자를 찾을수 없음"),
            @ApiResponse(code = 500, message = "SMS 전송 실패"),
    })
    @PostMapping("/accounts/authenticationKey")
    public SingleResponseResult<String> sendAuthenticationKey(@Valid @RequestBody SendAuthenticationKeyRequestDto sendAuthenticationKeyRequestDto) throws CoolsmsException {
        log.info("SignController_sendAuthenticationKey");
        return new SingleResponseResult<String> (smsService.sendAuthenticationKey(sendAuthenticationKeyRequestDto.getUserEmail()));
    }

    @ApiOperation(value = "계정잠금해제 인증번호 인증" , notes = "사용자가 잠금된 계정을 해제하기위해 인증번호를 인증")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "계정잠금해제 성공"),
            @ApiResponse(code = 408, message = "인증 요청한 사용자를 찾을수 없음"),
            @ApiResponse(code = 500, message = "SMS 전송 실패"),
    })
    @PostMapping("/accounts/authentication")
    public ResponseResult authAuthenticationKey(@Valid @RequestBody CertificateAuthenticationKeyRequestDto certificateAuthenticationKeyRequestDto) throws MessagingException {
        log.info("SignController_authAuthenticationKey");
        if(accountService.RequestUnLockAndIfSuccessSendTempPw(certificateAuthenticationKeyRequestDto))
            return ResponseResult.successResponse;
        else
            return ResponseResult.failResponse;
    }

    //계정 탈퇴?
}
