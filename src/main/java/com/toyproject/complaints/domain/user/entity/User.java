package com.toyproject.complaints.domain.user.entity;

import com.toyproject.complaints.domain.user.dto.response.LoginSuccessResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoListResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String userEmail;
    private String userPw;
    private String name;
    private String phoneNumber;
    private String employeeIdentificationNum;
    private int inValidAccessCnt;
    private String lockKey;
    private String refreshToken;
    private boolean active;
    private Role role;

    @OneToOne
    @JoinColumn(name = "register_user_id")
    private User registerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_user_id")
    private User updatedUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "updatedUser")
    private List<User> doUpdateUsers;

    @OneToMany(mappedBy = "user" , cascade =  CascadeType.ALL)
    private List<IpAddress> ipList = new ArrayList<>();

    //계정 생성시 등록자,수정자,등록시간,수정시간 초기화
    public void initialUserAndTimeAtCreateAccount(User adminAccount , String userPw){
        this.doUpdateUsers = new ArrayList<>();
        this.registerUser = adminAccount;
        this.userPw = "{noop}" + userPw;
        this.doUpdateUsers.add(adminAccount);
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.active = true;
    }

    //아이디,비밀번호 불일치시 불일치횟수 증가
    public void addInValidAccessCount() {
        this.inValidAccessCnt += 1;
    }

    //로그인 성공시 불일치횟수 초기화
    public void initializationInValidAccessCount(){
        this.inValidAccessCnt = 0;
    }

    //로그인 성공시 반환할 로그인 유저 정보
    public LoginSuccessResponseDto toLoginSuccessResponseDto(Role role) {
        return LoginSuccessResponseDto.builder().userId(id).userEmail(userEmail).role(role.getRole()).build();
    }

    //유저 리스트 정보 Dto
    public UserInfoListResponseDto toUserListInfoResponseDto(){
        String date = createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return UserInfoListResponseDto.builder().userId(id).name(name).email(userEmail)
                .employeeIdentificationNum(employeeIdentificationNum).phoneNumber(phoneNumber).regDate(date).build();
    }

    public UserInfoResponseDto toUserInfoResponseDto(){
        //계정에 등록된 ip들
        List<String> userIpList = new ArrayList<>();

        if(this.ipList.size() > 0){
            for (IpAddress ipAddress : this.ipList)
                userIpList.add(ipAddress.getIp());
        }
        String regDateTime = createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String updateDateTime = updatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return UserInfoResponseDto.builder().
                name(name).
                email(userEmail).
                phoneNumber(phoneNumber).
                active(active).
                userId(id).
                ipList(userIpList).
                employeeIdentificationNum(employeeIdentificationNum).
                regAdminName(registerUser.getName()).
                regAdminMail(registerUser.getUserEmail()).
                //updateAdminName(updateUser.getName()).
                //updateAdminEmail(updateUser.getUserEmail()).
                regDate(regDateTime).
                updateDate(updateDateTime).build();
    }
}
