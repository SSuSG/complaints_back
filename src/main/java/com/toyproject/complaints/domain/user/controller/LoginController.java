package com.toyproject.complaints.domain.user.controller;

import com.toyproject.complaints.domain.user.dto.request.LoginRequestDto;
import com.toyproject.complaints.domain.user.dto.response.LoginSuccessResponseDto;
import com.toyproject.complaints.domain.user.entity.LoginStatus;
import com.toyproject.complaints.domain.user.service.LoginService;
import com.toyproject.complaints.global.exception.LoginFailException;
import com.toyproject.complaints.global.response.ResponseResult;
import com.toyproject.complaints.global.response.SingleResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class LoginController {

    private final LoginService loginService;

    @ApiOperation(value = "로그인 시도" , notes = "사용자가 로그인을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "로그인 실패"),
            @ApiResponse(code = 406, message = "등록되지않은 IP에서 로그인 시도"),
            @ApiResponse(code = 406, message = "일치하지않는 PW"),
            @ApiResponse(code = 423, message = "잠금된 계정"),
    })
    @PostMapping("/users")
    public ResponseResult signIn(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) throws LoginFailException {
        log.info("LoginController_signIn -> 로그인");
        String loginResult = loginService.login(loginRequestDto, response);

        if(LoginStatus.SUCCESS.name().equals(loginResult)){
            return new SingleResponseResult<LoginSuccessResponseDto>(loginService.getLoginUserInfo(loginRequestDto));
        }else if(LoginStatus.LOCK.name().equals(loginResult)){
            return ResponseResult.LockResponse;
        }else if(LoginStatus.PWFAIL.name().equals(loginResult)){
            throw new LoginFailException();
        }else if(LoginStatus.IPFAIL.name().equals(loginResult)){
            throw new LoginFailException();
        }else{
            return ResponseResult.failResponse;
        }

    }
}
