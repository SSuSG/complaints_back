package com.toyproject.complaints.domain.user.controller;

import com.toyproject.complaints.domain.user.dto.request.CreateUserAccountRequestDto;
import com.toyproject.complaints.domain.user.service.AccountService;
import com.toyproject.complaints.global.response.ResponseResult;
import com.toyproject.complaints.global.response.SingleResponseResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "사용자 계정 생성" , notes = "슈퍼관리자가 사용자의 계정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "계정생성 성공"),
            @ApiResponse(code = 409, message = "중복이메일 존재"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음"),
            @ApiResponse(code = 500, message = "이메일 전송 실패"),
    })
    @PostMapping("/admins/users")
    public ResponseResult createUserAccount(@Valid @RequestBody CreateUserAccountRequestDto createUserAccountRequestDto) throws MessagingException {
        log.info("AccountController_createUserAccount || 관리자가 새로운 계정 생성");
        if(accountService.createUserAccount(createUserAccountRequestDto) != null){
            return ResponseResult.successResponse;
        }else{
            return ResponseResult.failResponse;
        }
    }
}
