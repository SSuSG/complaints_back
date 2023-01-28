package com.toyproject.complaints.domain.user.service;

import com.toyproject.complaints.domain.user.dto.request.ChangeIpRequestDto;
import com.toyproject.complaints.domain.user.entity.IpAddress;
import com.toyproject.complaints.domain.user.entity.Role;
import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoService {

    private final UserRepository userRepository;
    private final AccountService accountService;

    @Transactional
    public boolean addIpAddress(ChangeIpRequestDto changeIpRequestDto) throws ExistIpAddressException, UserNotFoundException, InValidAccessException, InValidIpException , AddIpFailException{
        log.info("UserInfoService_addIpAddress -> 아이피 추가");
        if(!isValidIpForm(changeIpRequestDto.getIp()))
            throw new InValidIpException();

        User curLoginUser = accountService.getLoginUser();
        User ipAddedUser = userRepository.findByUserEmail(changeIpRequestDto.getUserEmail()).orElseThrow(() -> new UserNotFoundException());

        if(Role.ADMIN.equals(curLoginUser.getRole()))
           addIpAddressInDB(ipAddedUser, changeIpRequestDto.getIp());
        else
            throw new InValidAccessException();
        return true;
    }

    private void addIpAddressInDB(User ipAddedUser , String addIpAddress){
        log.info("UserInfoService_addIpAddressInDB -> IP의 유효성 검사 및 추가");
        List<IpAddress> userIpList = ipAddedUser.getIpList();

        //IP는 최대 3개까지 등록 가능
        if(userIpList.size() == 3){
            throw new AddIpFailException();
        }else{
            if(hasIp(userIpList , addIpAddress))
                throw new ExistIpAddressException();

            IpAddress ipAddress = IpAddress.builder().ip(addIpAddress).user(ipAddedUser).build();
            userIpList.add(ipAddress);
        }
    }

    private boolean hasIp(List<IpAddress> userIpList , String addIpAddress){
        log.info("UserInfoService_hasIp -> 추가할 IP가 이미 존재하는지 확인");
        for (IpAddress ipAddress : userIpList) {
            if(addIpAddress.equals(ipAddress.getIp()))
                return true;
        }
        return false;
    }

    private boolean isValidIpForm(String ip){
        log.info("UserInfoService_isValidIpForm -> IP유효성 검사");
        String ipRegex = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";
        Pattern pattern = Pattern.compile(ipRegex);
        return pattern.matcher(ip).matches();
    }
}
