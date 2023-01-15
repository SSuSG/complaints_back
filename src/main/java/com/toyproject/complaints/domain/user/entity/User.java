package com.toyproject.complaints.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    /**
     * 이메일(아이디)
     */
    private String userEmail;

    /**
     *  비밀번호
     */
    private String userPw;

    /**
     *  이름
     */
    private String name;

    /**
     *  휴대폰 번호
     */
    private String phoneNumber;

    /**
     *  사번
     */
    private String employeeIdentificationNum;

    /**
     *  계정 불일치 횟수
     *  5회 이상 불일치시 계정잠금
     */
    private int inValidAccessCnt;

    /**
     *  계정 잠금 해제를 위한 KEY
     */
    private String lockKey;

    /**
     *  리프레쉬 토큰
     */
    private String refreshToken;

    /**
     *  탈퇴여부
     */
    private boolean active;

    /**
     *  계정 생성자
     */
    @OneToOne
    @JoinColumn(name = "registerUser_id")
    private User registerUser;

    /**
     *  계정 수정자
     */
    @OneToOne
    @JoinColumn(name = "updateUser_id")
    private User updateUser;


    /**
     *  IP주소
     */
    @OneToMany(mappedBy = "user" , cascade =  CascadeType.ALL)
    private List<IpAddress> ipList = new ArrayList<>();


}
