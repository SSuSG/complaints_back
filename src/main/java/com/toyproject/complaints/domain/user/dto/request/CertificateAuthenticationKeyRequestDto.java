package com.toyproject.complaints.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CertificateAuthenticationKeyRequestDto {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String authenticationKey;
}
