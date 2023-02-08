package com.toyproject.complaints.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("JwtAccessDeniedHandler_handle -> 허가받지 못한 권한에 접근시");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ResponseEntity<String> stringResponseEntity = new ResponseEntity<>("허가받지 못한 권한입니다.", HttpStatus.FORBIDDEN);
        String result = mapper.writeValueAsString(stringResponseEntity);
        response.getWriter().write(result);

//        response.sendError(HttpServletResponse.SC_FORBIDDEN , "허가받지 못한 권한입니다.");
    }
}
