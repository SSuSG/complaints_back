package com.toyproject.complaints.domain.complaints.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadVoiceFileRequestDto {
    private String complaintId;
    private String title;
    private MultipartFile file;
}
