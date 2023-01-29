package com.toyproject.complaints.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginSuccessResponseDto {
    private Long userId;
    private String userEmail;
    private String role;
}
