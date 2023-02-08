package com.toyproject.complaints.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("JwtAuthenticationEntryPoint_commence -> 인증실패시 실행");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ResponseEntity<String> stringResponseEntity = new ResponseEntity<>("로그인 먼저 해주세요..", HttpStatus.UNAUTHORIZED);
        String result = mapper.writeValueAsString(stringResponseEntity);
        response.getWriter().write(result);

//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "로그인 해주세요.");

    }
}
