package com.toyproject.complaints.domain.complaints.service;

import com.toyproject.complaints.domain.complaints.dto.Request.SearchComplaintsRequestDto;
import com.toyproject.complaints.domain.complaints.dto.Request.UploadVoiceFileRequestDto;
import com.toyproject.complaints.domain.complaints.dto.Response.ComplaintsResponseDto;
import com.toyproject.complaints.domain.complaints.entity.Complaints;
import com.toyproject.complaints.domain.complaints.repository.ComplaintsRepository;
import com.toyproject.complaints.global.aws.UploadFileToS3;
import com.toyproject.complaints.global.util.ClovaSpeechClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintsService {

    private final UploadFileToS3 uploadFileToS3;
    private final ClovaSpeechClient clovaSpeechClient;
    private final ComplaintsRepository complaintsRepository;

    /**
     * @throws IOException       ->S3에 파일업로드 실패
     * @throws ParseException    ->STT 결과 Parse 실패
     */
    @Transactional
    public void uploadVoiceFileAndSTT(UploadVoiceFileRequestDto uploadVoiceFileRequestDto) throws IOException, ParseException {
        log.info("ComplaintsService_uploadVoiceFileToS3 : CLOVA API 이용 STT후 S3에 저장");
        log.info("S3에 음성파일 업로드");
        String audioPath = uploadFileToS3.uploadVoiceFileToS3(uploadVoiceFileRequestDto);
        log.info("S3 음성파일 주소 : {}",audioPath);
        String content = voiceFileSTT(audioPath);

        /**
         * 파일 이름 -> 날짜_민원인_생일
         * ex) 20221122_박시균_980331
         * ex) 20221122_익명_000000
         */
        String[] fileName = uploadVoiceFileRequestDto.getFile().getOriginalFilename().split("_");
        String date = fileName[0];
        String complainName = fileName[1];
        String birthday = fileName[2];
        String title = fileName[3];
        Complaints complaints = Complaints.createComplaints(title,date,complainName,birthday,audioPath,content);
        complaintsRepository.save(complaints);
    }

    private String voiceFileSTT(String audioPath) throws ParseException {
        log.info("S3에서 음성파일 가져온후 STT");
        ClovaSpeechClient.NestRequestEntity requestEntity = new ClovaSpeechClient.NestRequestEntity();
        ClovaSpeechClient.Diarization diarization = new ClovaSpeechClient.Diarization();
        diarization.setEnable(true);
        diarization.setSpeakerCountMin(2);
        diarization.setSpeakerCountMax(2);
        requestEntity.setDiarization(diarization);

        //s3에 저장되어있는 파일 url을 가져와서 stt시키고 가져온 json으로
        String result = clovaSpeechClient.url(audioPath, requestEntity);
        log.info("STT RESULT : {}",result);

        JSONParser jsonParser = new JSONParser();
        JSONObject parse = (JSONObject) jsonParser.parse(result);
        log.info("STT JSON RESULT : {}",parse);
        JSONArray segments = (JSONArray) parse.get("segments");
        return sttResultToString(segments);
    }

    private String sttResultToString(JSONArray segments){
        log.info("STT결과 JSON TO STRING");
        String content = "";

        for (int i = 0; i < segments.size(); i++) {
            JSONObject segment = (JSONObject)segments.get(i);
            String text = (String)segment.get("text");
            JSONObject speaker = (JSONObject)segment.get("speaker");
            String name = (String)speaker.get("name");
            content += (name + " : " + text) + '\n';

        }
        log.info("STT결과 : {}",content);
        return content;
    }

    public List<ComplaintsResponseDto> searchComplaints(SearchComplaintsRequestDto searchComplaintsRequestDto) {
        log.info("ComplaintsService_searchComplaints : 민원 검색");
        String condition = searchComplaintsRequestDto.getCondition();
        String searchWord = searchComplaintsRequestDto.getSearchWord();

        if(condition.equals("민원인"))
            return complaintsRepository.findByComplaintId(searchWord).stream().map(o -> o.toDto()).collect(Collectors.toList());
        else if(condition.equals("민원 제목"))
            return complaintsRepository.findByTitleContaining(searchWord).stream().map( o -> o.toDto()).collect(Collectors.toList());
        else if(condition.equals("민원 내용"))
            return complaintsRepository.findByContentContaining(searchWord).stream().map( o -> o.toDto()).collect(Collectors.toList());
        else
            return complaintsRepository.findByContentContaining(searchWord).stream().map( o -> o.toDto()).collect(Collectors.toList());
    }

    public ComplaintsResponseDto getComplaints(Long id) {
        log.info("ComplaintsService_getComplaints : 민원 상세 조회");
        return complaintsRepository.findById(id).get().toDto();
    }
}
