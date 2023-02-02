package com.toyproject.complaints.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Slf4j
@Component
public class CheckForm {

    //이메일 형식 체크
    public boolean checkEmail(String email){
        //이메일 정규식
        String regx = "^[A-Za-z0-9+_.-]+@(.+)$";
        //정규식 패턴
        Pattern pattern = Pattern.compile(regx);

        return pattern.matcher(email).matches();
    }

    //휴대폰번호 형식 체크
    public boolean checkPhoneNumber(String phoneNumber){
        String regx = "\\d{11}";
        Pattern pattern = Pattern.compile(regx);

        return pattern.matcher(phoneNumber).matches();
    }
    //IP형식 체크
    public boolean checkIp(String ip){
        String ipRegex = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";
        Pattern pattern = Pattern.compile(ipRegex);

        return pattern.matcher(ip).matches();
    }

}
