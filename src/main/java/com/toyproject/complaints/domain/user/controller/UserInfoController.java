package com.toyproject.complaints.domain.user.controller;

import com.toyproject.complaints.domain.user.dto.request.ChangeIpRequestDto;
import com.toyproject.complaints.domain.user.service.UserInfoService;
import com.toyproject.complaints.global.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserInfoController {

    private final UserInfoService userInfoService;

    //아이피 추가
    @PostMapping("/ip")
    public ResponseResult addIpAddress(@Valid @RequestBody ChangeIpRequestDto changeIpRequestDto) {
        if(userInfoService.addIpAddress(changeIpRequestDto)){
            return ResponseResult.successResponse;
        }else{
            return ResponseResult.failResponse;
        }
    }

    //관리자 리스트 조회

    //관리자 한명 조회

    //관리자 이메일 수정

    //관리자 휴대폰 수정

    //관리자 탈퇴 기능?

    //마이페이지 정보 반환환
}
