package com.toyproject.complaints.global.security;

import com.toyproject.complaints.domain.user.entity.UserDetailsImpl;
import com.toyproject.complaints.domain.user.repository.UserRepository;
import com.toyproject.complaints.domain.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {


    @Value("spring.jwt.secret")
    private String secretKey;

    private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 14;
    private long accessTokenValidTime = 1000L * 60 * 60; // 1시간 토큰 유효

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String userEmail){
        log.info("JwtTokenProvider_createAccessToken -> ACCESS TOKEN 생성");
        return this.createToken(userEmail, accessTokenValidTime);
    }

    public String createRefreshToken(String userEmail) {
        log.info("JwtTokenProvider_createRefreshToken -> REFRESH_TOKEN 생성");
        return this.createToken(userEmail,  refreshTokenValidTime);
    }

    public String createToken(String userEmail, long tokenValid) {
        log.info("JwtTokenProvider_createToken -> JWT토큰 생성");
        Claims claims = Jwts.claims().setSubject(userEmail);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValid)) // 토큰 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, 암호키
                .compact();
    }

    public Authentication getAuthentication(String token) {
        log.info("JwtTokenProvider_getAuthentication -> JWT토큰으로 인증 정보 조회");
        UserDetailsImpl userDetails = (UserDetailsImpl)customUserDetailsService.loadUserByUsername(getUserEmail(token));
        //log.info("UserName : {}" , userDetails.getUsername());
        //log.info("UserPw : {}" , userDetails.getPassword());
        return new UsernamePasswordAuthenticationToken(userDetails.getUser().getUserEmail(), "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        log.info("JwtTokenProvider_getUserEmail -> JWT토큰에서 회원의 이메일 정보 추출");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getAccessToken(HttpServletRequest request) {
        log.info("JwtTokenProvider_getAccessToken -> Request의 Header에서 AccessToken 값을 가져옵니다.");
        if(request.getHeader("X-AUTH-TOKEN") != null )
            return request.getHeader("X-AUTH-TOKEN");
        return null;
    }

    public String getRefreshToken(HttpServletRequest request) {
        log.info("JwtTokenProvider_getRefreshToken -> Request의 Header에서 RefreshToken 값을 가져옵니다.");
        if(request.getHeader("X-AUTH-REFRESH-TOKEN") != null )
            return request.getHeader("X-AUTH-REFRESH-TOKEN");
        return null;
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        log.info("JwtTokenProvider_validateToken -> JWT토큰의 유효성 + 만료일자 확인");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            if(claims.getBody().getExpiration().getTime() - claims.getBody().getIssuedAt().getTime() != accessTokenValidTime){
                log.info("토큰의 시간이 만료되었습니다.");
                return false;
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        log.info("JwtTokenProvider_setHeaderAccessToken -> ACCESS TOKEN 헤더 설정");
        response.setHeader("X-AUTH-TOKEN", accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        log.info("JwtTokenProvider_setHeaderRefreshToken -> REFRESH TOKEN 헤더 설정");
        response.setHeader("X-AUTH-REFRESH-TOKEN", refreshToken);
    }

    public boolean existRefreshToken(String refreshToken) {
        log.info("JwtTokenProvider_existsRefreshToken -> REFRESH TOKEN 존재유무 확인");
        return userRepository.existsByRefreshToken(refreshToken);
    }



}
