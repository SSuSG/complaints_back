package com.toyproject.complaints.domain.user.service;

import com.toyproject.complaints.domain.user.dto.request.LoginRequestDto;
import com.toyproject.complaints.domain.user.dto.response.LoginSuccessResponseDto;
import com.toyproject.complaints.domain.user.entity.IpAddress;
import com.toyproject.complaints.domain.user.entity.LoginStatus;
import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.global.exception.LoginFailException;
import com.toyproject.complaints.global.exception.UserNotFoundException;
import com.toyproject.complaints.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //로그인
    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) throws UserNotFoundException, LoginFailException {
        log.info("LoginService_login -> 로그인 시도");
        User loginUser = userRepository.findByUserEmail(loginRequestDto.getUserEmail()).orElseThrow( () -> new UserNotFoundException());

        if(loginUser.getInValidAccessCnt() >= 5){
            log.info("현재 로그인 시도계정은 잠금상태입니다.");
            return LoginStatus.LOCK.name();
        }

        if(!passwordEncoder.matches(loginRequestDto.getUserPw() , loginUser.getUserPw())){
            log.info("로그인 실패 , 비밀번호 틀린횟수 : {}" , loginUser.getInValidAccessCnt());
            loginUser.addInValidAccessCount();
            return LoginStatus.PWFAIL.name();
        }else{
            log.info("로그인 성공 , IP값 유효성 확인");
            log.info("접속 ip : {}" , loginRequestDto.getIpAddress());
            boolean isValidIpAddress = compareIpAddressAfterSuccessLogin(loginUser , loginRequestDto.getIpAddress());

            if(isValidIpAddress){
                log.info("접속시도 IP와 등록된 IP일치");
                settingTokenAfterSuccessLogin(loginUser ,response);
                return LoginStatus.SUCCESS.name();
            }else{
                log.info("접속시도 IP와 등록된 IP일치실패");
                return LoginStatus.IPFAIL.name();
            }
        }
    }
    private boolean compareIpAddressAfterSuccessLogin(User loginUser , String accessIp){
        log.info("LoginService_compareIpAddressAfterSuccessLogin");

        if(loginUser.getIpList().size() == 0){
            log.info("첫 로그인 시 현재 ip 저장");
            IpAddress ipAddress = IpAddress.builder().ip(accessIp).user(loginUser).build();
            loginUser.getIpList().add(ipAddress);
            return true;
        }else{
            for (IpAddress ipAddress : loginUser.getIpList()) {
                //현재 ip가 등록된 ip와 같으면 로그인 성공
                if(accessIp.equals(ipAddress.getIp()))
                    return true;
            }
        }
        return false;
    }

    private void settingTokenAfterSuccessLogin(User loginUser , HttpServletResponse response){
        log.info("LoginService_settingTokenAfterSuccessLogin -> 로그인 시도 성공시 TOKEN을 RESPONSE에 주입");
        String accessToken = jwtTokenProvider.createAccessToken(loginUser.getUserEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser.getUserEmail());

        //response에 토큰들 담아줌
        jwtTokenProvider.setHeaderAccessToken(response,accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response,refreshToken);

        //유저 db에 refreshToKen저장
        loginUser.setRefreshToken(refreshToken);

        //로그인 성공시 비밀번호 불일치횟수 초기화
        loginUser.initializationInValidAccessCount();
    }

    //로그인시 계정의 권한,이메일,유저id 반환
    public LoginSuccessResponseDto getLoginUserInfo(LoginRequestDto loginRequestDto){
        log.info("LoginService_getLoginUserInfo -> 로그인 성공후 로그인 시도 유저 정보 반환");
        User loginUser = userRepository.findByUserEmail(loginRequestDto.getUserEmail()).orElseThrow( () -> new UserNotFoundException());
        return loginUser.toLoginSuccessResponseDto(loginUser.getRole());

    }

}
