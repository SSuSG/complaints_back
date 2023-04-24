package com.toyproject.complaints.domain.complaints.dto.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchComplaintsRequestDto {
    @NotBlank
    private String searchWord;
    @NotBlank
    private String condition;
}
