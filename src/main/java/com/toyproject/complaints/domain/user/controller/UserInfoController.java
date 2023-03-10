package com.toyproject.complaints.domain.user.controller;

import com.toyproject.complaints.domain.user.dto.request.ChangeIpRequestDto;
import com.toyproject.complaints.domain.user.dto.request.UpdateEmailRequestDto;
import com.toyproject.complaints.domain.user.dto.request.UpdatePhoneNumberRequestDto;
import com.toyproject.complaints.domain.user.dto.response.MyPageResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoListResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoResponseDto;
import com.toyproject.complaints.domain.user.service.UserInfoService;
import com.toyproject.complaints.global.response.ListResponseResult;
import com.toyproject.complaints.global.response.ResponseResult;
import com.toyproject.complaints.global.response.SingleResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @ApiOperation(value = "아이피 추가" , notes = "슈퍼관리자가 사용자계정에 IP 추가등록을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "IP 추가 성공"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 406, message = "이미 존재하는 IP는 추가 불가"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음"),
            @ApiResponse(code = 410, message = "등록할 수 있는 IP의 수를 초과"),
    })
    @PostMapping("admins/ip")
    public ResponseResult addIpAddress(@Valid @RequestBody ChangeIpRequestDto changeIpRequestDto) {
        if(userInfoService.addIpAddress(changeIpRequestDto))
            return ResponseResult.successResponse;
        return ResponseResult.failResponse;
    }

    @ApiOperation(value = "아이피 삭제" , notes = "슈퍼관리자가 사용자계정에 IP 삭제를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "IP 추가 성공"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음"),
            @ApiResponse(code = 410, message = "삭제할 수 있는 IP가 존재하지 않음"),
    })
    @DeleteMapping("admins/ip")
    public ResponseResult deleteIpAddress(@Valid @RequestBody ChangeIpRequestDto changeIpRequestDto){
        if(userInfoService.deleteIpAddress(changeIpRequestDto))
            return ResponseResult.successResponse;
        return ResponseResult.failResponse;
    }

    @ApiOperation(value = "유저 리스트 조회" , notes = "전체 유저정보 반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "전체 유저정보 반환 성공"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음"),
            @ApiResponse(code = 408, message = "조회할 수 있는 유저가 1명도 없음")
    })
    @GetMapping("/admins/users")
    public ListResponseResult<UserInfoListResponseDto> getUserList(){
        return new ListResponseResult<>(userInfoService.getUserList());
    }

    @ApiOperation(value = "유저 조회" , notes = "유저정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저정보 반환 성공"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음"),
            @ApiResponse(code = 408, message = "조회할 수 있는 유저가 1명도 없음")
    })
    @GetMapping("admins/users/{userId}")
    public SingleResponseResult<UserInfoResponseDto> findUserInfo(@PathVariable Long userId){
        return new SingleResponseResult<>(userInfoService.findUserInfo(userId));
    }

    @ApiOperation(value = "관리자 이메일 수정" , notes = "관리자가 유저의 이메일 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관리자가 유저의 이메일 수정 성공"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 406, message = "이메일 형식이 잘못됌"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음")
    })
    @PostMapping("admins/users/email")
    public ResponseResult updateEmail(@Valid @RequestBody UpdateEmailRequestDto updateEmailRequestDto){
        if(userInfoService.updateEmail(updateEmailRequestDto)){
            return ResponseResult.successResponse;
        }else{
            return ResponseResult.failResponse;
        }
    }

    @ApiOperation(value = "관리자 관리자 휴대폰 수정" , notes = "관리자가 유저의 관리자 휴대폰 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관리자가 유저의 관리자 휴대폰 수정"),
            @ApiResponse(code = 403, message = "관리자권한이 없는 사용자의 접근"),
            @ApiResponse(code = 406, message = "휴대폰 형식이 잘못됌"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음")
    })
    @PostMapping("/admin/user/phoneNumber")
    public ResponseResult updatePhoneAndEmail(@Valid @RequestBody UpdatePhoneNumberRequestDto updatePhoneNumberRequestDto){
        if(userInfoService.updatePhoneNumber(updatePhoneNumberRequestDto)){
            return ResponseResult.successResponse;
        }else{
            return ResponseResult.failResponse;
        }
    }

    @ApiOperation(value = "마이페이지 정보 반환" , notes = "마이페이지 정보 반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "마이페이지 정보 반환 성공"),
            @ApiResponse(code = 403, message = "접근한 유저페이지와 로그인정보가 일치하지 않는 경우"),
            @ApiResponse(code = 408, message = "현재 로그인한 관리자를 찾을수 없음")
    })
    @GetMapping("/users/{userId}")
    public SingleResponseResult getMyPageInfo(@PathVariable Long userId){
        return new SingleResponseResult<MyPageResponseDto>(userInfoService.getMyPageInfo(userId));
    }



    //관리자 탈퇴 기능?

}
