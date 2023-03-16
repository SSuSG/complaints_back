package com.toyproject.complaints.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Slf4j
@Component
public class CheckForm {

    public boolean checkEmail(String email){
        log.info("CheckForm_checkEmail -> 이메일 형식 검사");
        //이메일 정규식
        String regx = "^[A-Za-z0-9+_.-]+@(.+)$";
        //정규식 패턴
        Pattern pattern = Pattern.compile(regx);

        return pattern.matcher(email).matches();
    }


    public boolean checkPhoneNumber(String phoneNumber){
        log.info("CheckForm_checkPhoneNumber -> 휴대폰번호 유효성 검사");
        String regx = "\\d{11}";
        Pattern pattern = Pattern.compile(regx);

        return pattern.matcher(phoneNumber).matches();
    }

    public boolean checkIp(String ip){
        log.info("CheckForm_checkIp -> IP 형식 검사");
        String ipRegex = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";
        Pattern pattern = Pattern.compile(ipRegex);

        return pattern.matcher(ip).matches();
    }

}
