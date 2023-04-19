package com.toyproject.complaints.global.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.toyproject.complaints.domain.complaints.dto.UploadVoiceFileRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class UploadFileToS3 {

    @Value("${aws.s3.bucket}")
    private String S3Bucket;

    private final AmazonS3Client amazonS3Client;

    public String uploadVoiceFileToS3(UploadVoiceFileRequestDto uploadVoiceFileRequestDto) throws IOException {
        log.info("UploadFileToS3_uploadVoiceFileToS3 : S3로 음성파일 업로드");
        String originalName = uploadVoiceFileRequestDto.getFile().getOriginalFilename(); // 파일 이름
        long size = uploadVoiceFileRequestDto.getFile().getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(uploadVoiceFileRequestDto.getFile().getContentType());
        objectMetaData.setContentLength(size);

        // S3에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, originalName, uploadVoiceFileRequestDto.getFile().getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String audioPath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
        return audioPath;
    }
}
