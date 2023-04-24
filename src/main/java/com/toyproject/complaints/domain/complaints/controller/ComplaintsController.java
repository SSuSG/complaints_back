package com.toyproject.complaints.domain.complaints.controller;

import com.toyproject.complaints.domain.complaints.dto.Request.SearchComplaintsRequestDto;
import com.toyproject.complaints.domain.complaints.dto.Request.UploadVoiceFileRequestDto;
import com.toyproject.complaints.domain.complaints.dto.Response.ComplaintsResponseDto;
import com.toyproject.complaints.domain.complaints.service.ComplaintsService;
import com.toyproject.complaints.global.response.ListResponseResult;
import com.toyproject.complaints.global.response.ResponseResult;
import com.toyproject.complaints.global.response.SingleResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ComplaintsController {

    private final ComplaintsService complaintsService;

    @ApiOperation(value = "음성파일 업로드,STT" , notes = "음성파일을 업로드하면 STT 한 후 S3에 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "STT,S3에 업로드 성공"),
            @ApiResponse(code = 412, message = "S3에 파일업로드가 실패"),
            @ApiResponse(code = 413, message = "STT결과를 PARSE도중에 문제가 발생"),
    })
    @PostMapping(value = "/voices")
    public ResponseResult uploadVoiceFile(@ModelAttribute UploadVoiceFileRequestDto uploadVoiceFileRequestDto) throws IOException, ParseException {
        log.info("ComplaintsController_uploadVoiceFile");
        complaintsService.uploadVoiceFileAndSTT(uploadVoiceFileRequestDto);
        return ResponseResult.successResponse;
    }

    @ApiOperation(value = "민원 검색" , notes = "민원인,민원제목을 통한 민원검색")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "민원 검색 성공"),
            @ApiResponse(code = 412, message = "민원 검색도중 에러 발생"),
    })
    @PostMapping("/complaints")
    public ListResponseResult<ComplaintsResponseDto> searchComplaints(@Valid @RequestBody SearchComplaintsRequestDto searchComplaintsRequestDto){
        log.info("ComplaintsController_searchComplaints : 민원인 검색 컨트롤러");
        log.info("검색조건 : {}" , searchComplaintsRequestDto.getCondition());
        log.info("검색어 : {}" , searchComplaintsRequestDto.getSearchWord());
        List<ComplaintsResponseDto> ComplaintsResponseDtos = complaintsService.searchComplaints(searchComplaintsRequestDto);
        for (ComplaintsResponseDto complaintsResponseDto : ComplaintsResponseDtos) {
            log.info("제목 : {}" , complaintsResponseDto.getTitle());
        }
        return new ListResponseResult<>(ComplaintsResponseDtos);
    }

    @ApiOperation(value = "민원 조회" , notes = "민원 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "민원 조회 성공"),
            @ApiResponse(code = 412, message = "민원 조회도중 에러 발생"),
    })
    @GetMapping(value = "/complaints/{id}")
    public SingleResponseResult getComplaints(@PathVariable Long id) {
        log.info("ComplaintsController_getComplaints : 민원 조회" );
        ComplaintsResponseDto complaintsResponseDto = complaintsService.getComplaints(id);
        return new SingleResponseResult<>(complaintsResponseDto);
    }

}
