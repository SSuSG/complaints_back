package com.toyproject.complaints.domain.user.entity;

import com.toyproject.complaints.domain.user.dto.response.LoginSuccessResponseDto;
import com.toyproject.complaints.domain.user.dto.response.MyPageResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoListResponseDto;
import com.toyproject.complaints.domain.user.dto.response.UserInfoResponseDto;
import lombok.*;
import org.hibernate.envers.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@AuditTable("user_audit")
@AuditOverride(forClass=BaseEntity.class)
public class User extends BaseEntity{

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

    //@NotAudited                               //연관관계 테이블 생성도 하지 않고 추적하지도 않을거라면 엔티티 필드에 다음과 같은 어노테이션을 추가
    //@Audited(targetAuditMode = NOT_AUDITED)   //연관관계 추적 테이블 생성은 되지만 추적하진 않을거라면 엔티티 내 필드에 다음과 같이 어노테이션을 추가
    @OneToMany(mappedBy = "user" , cascade =  CascadeType.ALL)
    //@AuditMappedBy(mappedBy = "user")
    private List<IpAddress> ipList = new ArrayList<>();

    //계정 생성시 비밀번호 저장
    public void initialUserPw(String userPw){
        this.userPw = "{noop}" + userPw;
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

    //Todo update 기록에 대한 엔티티 만들기
    public void updateEmail(String updateEmail){
        this.updatedTime = LocalDateTime.now();
        this.userEmail = updateEmail;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.updatedTime = LocalDateTime.now();
        this.phoneNumber = phoneNumber;
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
                //regAdminName(registerUser.getName()).
                //regAdminMail(registerUser.getUserEmail()).
                //updateAdminName(updateUser.getName()).
                //updateAdminEmail(updateUser.getUserEmail()).
                regDate(regDateTime).
                updateDate(updateDateTime).build();
    }


    public MyPageResponseDto toMyPageResponseDto() {
        //계정에 등록된 ip들
        List<String> userIpList = new ArrayList<>();

        if(this.ipList.size() > 0){
            for (IpAddress ipAddress : this.ipList) {
                userIpList.add(ipAddress.getIp());
            }
        }
        return MyPageResponseDto.builder().name(name).email(userEmail).phoneNumber(phoneNumber)
                .userId(id).ipList(userIpList).employeeIdentificationNum(employeeIdentificationNum).build();
    }
}
