package com.toyproject.complaints.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class UserInfoResponseDto {

    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String employeeIdentificationNum;
    private String regAdminName;
    private String regAdminMail;
    private List<String> ipList;
    private String regDate;
    //private String updateAdminName;
    //private String updateAdminEmail;
    private String updateDate;
    private boolean active;

}
