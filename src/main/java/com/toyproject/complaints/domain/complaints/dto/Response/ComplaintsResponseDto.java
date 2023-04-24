package com.toyproject.complaints.domain.complaints.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ComplaintsResponseDto {
    private Long id;
    private String complaintsName;
    private String audioPath;
    private String title;
    private String content;
    private String birthday;
    private String date;
    private String summary;
    //private String[] tagList;
}
