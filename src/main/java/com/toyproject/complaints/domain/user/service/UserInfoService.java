package com.toyproject.complaints.domain.user.service;

import com.toyproject.complaints.domain.user.dto.request.ChangeIpRequestDto;
import com.toyproject.complaints.domain.user.dto.request.UpdateEmailRequestDto;
import com.toyproject.complaints.domain.user.dto.request.UpdatePhoneNumberRequestDto;
import com.toyproject.complaints.domain.user.dto.response.MyPageResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoListResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoResponseDto;
import com.toyproject.complaints.domain.user.entity.IpAddress;
import com.toyproject.complaints.domain.user.entity.Role;
import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.global.exception.*;
import com.toyproject.complaints.global.util.CheckForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final CheckForm checkForm;

    /**
     * @throws ExistIpAddressException  -> 추가할 IP가 이미 존재하는 경우
     * @throws UserNotFoundException    -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     * @throws InValidAccessException   -> 슈퍼관라자가 아닌 사용자가 접근한 경우
     * @throws InValidIpException       -> 추가할 IP가 형식이 올바르지 않은 경우
     * @throws AddIpFailException       -> 계정에 등록된 IP갯수가 3개여서 더 이상 추가하지 못하는 경우
     */
    @Transactional
    public boolean addIpAddress(ChangeIpRequestDto changeIpRequestDto) throws ExistIpAddressException, UserNotFoundException, InValidAccessException, InValidIpException , AddIpFailException{
        log.info("UserInfoService_addIpAddress -> 아이피 추가");
        if(!checkForm.checkIp(changeIpRequestDto.getIp()))
            throw new InValidIpException();

        User curLoginUser = accountService.getLoginUser();
        User ipAddedUser = userRepository.findByUserEmail(changeIpRequestDto.getUserEmail()).orElseThrow(() -> new UserNotFoundException());

        if(!Role.ADMIN.equals(curLoginUser.getRole()))
            throw new InValidAccessException();
        addIpAddressInDB(ipAddedUser, changeIpRequestDto.getIp());
        return true;
    }

    private void addIpAddressInDB(User ipAddedUser , String addIpAddress){
        log.info("UserInfoService_addIpAddressInDB -> IP의 유효성 검사 및 추가");
        List<IpAddress> userIpList = ipAddedUser.getIpList();

        //IP는 최대 3개까지 등록 가능
        if(userIpList.size() == 3)
            throw new AddIpFailException();

        if(hasIp(userIpList , addIpAddress))
            throw new ExistIpAddressException();

        IpAddress ipAddress = IpAddress.builder().ip(addIpAddress).user(ipAddedUser).build();
        userIpList.add(ipAddress);
    }

    private boolean hasIp(List<IpAddress> userIpList , String addIpAddress){
        log.info("UserInfoService_hasIp -> 추가할 IP가 이미 존재하는지 확인");
        for (IpAddress ipAddress : userIpList) {
            if(addIpAddress.equals(ipAddress.getIp()))
                return true;
        }
        return false;
    }

    /**
     * @throws UserNotFoundException    -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     * @throws InValidAccessException   -> 슈퍼관라자가 아닌 사용자가 접근한 경우
     * @throws DeleteIpFailException    -> Ip의 갯수가 1개 미만일 경우
     */
    @Transactional
    public boolean deleteIpAddress(ChangeIpRequestDto changeIpRequestDto) {
        log.info("UserInfoService_deleteIpAddress -> 아이피 삭제");
        User curLoginUser = accountService.getLoginUser();
        User ipDeletedUser = userRepository.findByUserEmail(changeIpRequestDto.getUserEmail()).orElseThrow(() -> new UserNotFoundException());

        if(!Role.ADMIN.equals(curLoginUser.getRole()))
            throw new InValidAccessException();

        if(deleteIpAddressInDb(ipDeletedUser , changeIpRequestDto.getIp()))
            return true;
        return false;
    }

    private boolean deleteIpAddressInDb(User ipDeletedUser , String deleteIpAddress) {
        log.info("UserInfoService_deleteIpAddressInDb -> 아이피 삭제 구체적인 로직");
        List<IpAddress> ipList = ipDeletedUser.getIpList();

        if(ipList.size() < 1)
            throw new DeleteIpFailException();

        for(int i = 0 ; i < ipList.size() ; i++){
            if(ipList.get(i).getIp().equals(deleteIpAddress)){
                ipList.get(i).setUser(null);
                return true;
            }
        }

        return false;
    }

    /**
     * @throws FailReadUserException    -> 조회할 수 있는 유저가 1명도 없을경우
     * @throws UserNotFoundException    -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     * @throws InValidAccessException   -> 슈퍼관라자가 아닌 사용자가 접근한 경우
     */
    public List<UserInfoListResponseDto> getUserList() {
        log.info("UserInfoService_getUserList -> 전체 유저 정보 조회");
        List<User> userList = userRepository.findByActive(true);

        if(userList.size() == 0)
            throw new FailReadUserException();

        return userList.stream().map( u -> u.toUserListInfoResponseDto()).collect(Collectors.toList());
    }

    /**
     * @throws UserNotFoundException    -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     * @throws InValidAccessException   -> 슈퍼관라자가 아닌 사용자가 접근한 경우
     */
    public UserInfoResponseDto findUserInfo(Long userId) {
        log.info("UserInfoService_findUserInfo -> 유저 정보 조회");
        User curLoginUser = accountService.getLoginUser();

        if(!Role.ADMIN.equals(curLoginUser.getRole()))
            throw new InValidAccessException();

        return userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException()).toUserInfoResponseDto();
    }


    /**
     * @throws UserNotFoundException    -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     * @throws InValidEmailException    -> 이메일 형식이 올바르지 않은경우
     * @throws InValidAccessException   -> 슈퍼관리자가 아닌 사용자가 접근한 경우
     */
    @Transactional
    public boolean updateEmail(UpdateEmailRequestDto updateEmailRequestDto){
        log.info("UserInfoService_updateEmail -> 유저 이메일 수정");
        User updatedUser = userRepository.findById(updateEmailRequestDto.getId()).orElseThrow( () -> new UserNotFoundException());
        User curLoginUser = accountService.getLoginUser();

        if(!checkForm.checkEmail(updateEmailRequestDto.getEmail()))
            throw new InValidEmailException();

        if(!Role.ADMIN.equals(curLoginUser.getRole()))
            throw new InValidAccessException();

        updatedUser.updateEmail(updateEmailRequestDto.getEmail());

        if(updatedUser.getUserEmail().equals(updateEmailRequestDto.getEmail()))
            return true;
        return false;
    }

    /**
     * @throws UserNotFoundException        -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     * @throws InValidPhoneNumberException  -> 휴대폰 형식이 올바르지 않은경우
     * @throws InValidAccessException       -> 슈퍼관리자가 아닌 사용자가 접근한 경우
     */
    @Transactional
    public boolean updatePhoneNumber(UpdatePhoneNumberRequestDto updatePhoneNumberRequestDto){
        log.info("UserInfoService_updatePhoneNumber -> 유저 휴대폰번호 수정");
        User updatedUser = userRepository.findById(updatePhoneNumberRequestDto.getId()).orElseThrow( () -> new UserNotFoundException());
        User curLoginUser = accountService.getLoginUser();

        if(!checkForm.checkPhoneNumber(updatePhoneNumberRequestDto.getPhoneNumber()))
            throw new InValidPhoneNumberException();

        if(!Role.ADMIN.equals(curLoginUser.getRole()))
            throw new InValidAccessException();

        updatedUser.updatePhoneNumber(updatePhoneNumberRequestDto.getPhoneNumber());
        if(updatedUser.getPhoneNumber().equals(updatePhoneNumberRequestDto.getPhoneNumber()))
            return true;
        return false;
    }

    /**
     * @throws UserNotFoundException        -> 현재 사용자의 계정이 존재하지 않는경우
     * @throws InValidAccessException       -> 접근한 유저페이지와 로그인정보가 일치하지 않는 경우
     */
    public MyPageResponseDto getMyPageInfo(Long userId) {
        log.info("UserInfoService_getMyPageInfo -> 마이 페이지 조회");
        User curLoginUser = accountService.getLoginUser();
        if(!curLoginUser.getId().equals(userId))
            throw new InValidAccessException();

        return userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException()).toMyPageResponseDto();
    }
}
