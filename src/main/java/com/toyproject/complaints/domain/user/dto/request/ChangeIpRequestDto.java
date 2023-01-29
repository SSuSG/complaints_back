package com.toyproject.complaints.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangeIpRequestDto {

    @NotBlank
    private String ip;

    @NotBlank
    private String userEmail;
}
