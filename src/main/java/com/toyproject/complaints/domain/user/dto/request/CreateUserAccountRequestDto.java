package com.toyproject.complaints.domain.user.dto.request;

import com.toyproject.complaints.domain.user.entity.Role;
import com.toyproject.complaints.domain.user.entity.User;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
public class CreateUserAccountRequestDto {

    @NotBlank
    private String userEmail;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String employeeIdentificationNum;

    public User toUserEntity(){
        return User.builder()
                .userEmail(userEmail)
                .name(name)
                .phoneNumber(phoneNumber)
                .employeeIdentificationNum(employeeIdentificationNum)
                .role(Role.USER)
                //.roles(Collections.singletonList("ROLE_USER"))
                //.department(department)
                .build();
    }
}
