package com.toyproject.complaints.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class MyPageResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String employeeIdentificationNum;
    private List<String> ipList;
}
