package com.toyproject.complaints.domain.user.service;

import com.toyproject.complaints.domain.user.dto.request.CreateUserAccountRequestDto;
import com.toyproject.complaints.domain.user.entity.Role;
import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.global.exception.ExistEmailException;
import com.toyproject.complaints.global.exception.InValidAccessException;
import com.toyproject.complaints.global.exception.UserNotFoundException;
import com.toyproject.complaints.global.util.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mail mailService;

    /**
     *
     * @throws ExistEmailException      -> 신규계정 생성시 이미 존재하는 이메일이 존재할 경우
     * @throws MessagingException       -> 신규계정 성공후 이메일로 임시비밀번호 발송에 실패 할 경우
     * @throws InValidAccessException   -> 신규계정 생성시도를 슈퍼관리자권한을 가지지 못한 사용자가 했을경우
     * @throws UserNotFoundException    -> 현재 로그인한 사용자의 계정이 존재하지 않는경우
     */
    @Transactional
    public Long createUserAccount(CreateUserAccountRequestDto createUserAccountRequestDto) throws ExistEmailException , MessagingException , InValidAccessException , UserNotFoundException {
        log.info("AccountService_createUserAccount -> 관리자에 의한 신규계정 생성");
        User createdAccount = createUserAccountRequestDto.toUserEntity();

        //현재 로그인한 계정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        User curLoginUser = userRepository.findByUserEmail(loginId).orElseThrow(() -> new UserNotFoundException());

        if(Role.ADMIN.equals(curLoginUser.getRole())){
            if(checkEmailDuplicate(createUserAccountRequestDto.getUserEmail())){
                throw new ExistEmailException();
            }else {
                log.info("임시 비밀번호 생성");
                String tempPw = mailService.createKey();

                log.info("계정 성공 생성시 이메일로 임시비밀번호 발송");
                if(!mailService.sendSimpleMessageForTempPw(createdAccount.getUserEmail() , tempPw)){
                    throw new MessagingException();
                }

                log.info("생성된 계정의 등록자,수정자,등록일,수정일,비밀번호 초기화");
                createdAccount.initialUserAndTimeAtCreateAccount(curLoginUser , tempPw);

                return userRepository.save(createdAccount).getId();
            }
        }else{
            throw new InValidAccessException();
        }
    }

    public boolean checkEmailDuplicate(String email) {
        log.info("AccountService_checkEmailDuplicate -> 계정생성시 이메일 중복 체크");
        if(userRepository.existsByUserEmail(email))
            return true;
        return false;
    }
}
