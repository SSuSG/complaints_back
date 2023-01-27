package com.toyproject.complaints.domain.user.service;

import com.toyproject.complaints.domain.user.entity.User;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Random;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsService {

    private final UserRepository userRepository;

    @Value("${COOL.API_KEY}")
    private String apiKey;

    @Value("${COOL.API_SECRET_KEY}")
    private String apiSecretKey;

    @Value("${COOL.SENDER_NUMBER}")
    private String sender;

    @Transactional
    public String sendAuthenticationKey(String userEmail) throws UserNotFoundException, CoolsmsException {
        log.info("SmsService_sendAuthenticationKey -> 계정잠금 해제를위한 sms전송");
        User loginUser = userRepository.findByUserEmail(userEmail).orElseThrow( () -> new UserNotFoundException());
        Message coolsms = new Message(apiKey, apiSecretKey);
        String authenticationKey = CreateAuthenticationKey();
        coolsms.send(settingSms(loginUser, authenticationKey));
        loginUser.setLockKey(authenticationKey);
        return authenticationKey;
    }

    private HashMap<String, String> settingSms(User loginUser, String authenticationKey) {
        log.info("인증번호발송 SMS 설정");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", loginUser.getPhoneNumber());    // 수신전화번호 (ajax로 view 화면에서 받아온 값으로 넘김)
        params.put("from", sender);    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + authenticationKey + "] 입니다.");
        return params;
    }

    private String CreateAuthenticationKey(){
        log.info("인증키 생성");
        Random rand  = new Random();
        String authenticationKey ="";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            authenticationKey +=ran;
        }

        return authenticationKey;
    }
}
