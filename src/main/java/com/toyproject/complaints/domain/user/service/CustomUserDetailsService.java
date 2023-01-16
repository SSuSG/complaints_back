package com.toyproject.complaints.domain.user.service;

import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.domain.user.entity.UserDetailsImpl;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailService_loadUserByUsername");

        User findUser = userRepository.findByUserEmail(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if(findUser != null){
            UserDetailsImpl userDetails = new UserDetailsImpl(findUser);
            return  userDetails;
        }

        return null;
    }
}
