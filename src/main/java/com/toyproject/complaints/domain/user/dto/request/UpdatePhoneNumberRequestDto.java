package com.toyproject.complaints.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdatePhoneNumberRequestDto {
    @NotNull
    private Long id;

    @NotBlank
    private String phoneNumber;
}
