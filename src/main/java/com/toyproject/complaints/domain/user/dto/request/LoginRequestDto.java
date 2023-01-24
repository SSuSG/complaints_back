package com.toyproject.complaints.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank
    private String userEmail;

    @NotBlank
    private String userPw;

    @NotBlank
    private String ipAddress;
}
