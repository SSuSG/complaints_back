package com.toyproject.complaints.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoListResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String employeeIdentificationNum;
    private String regDate;
}
