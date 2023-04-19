package com.toyproject.complaints.domain.complaints.entity;

import com.toyproject.complaints.domain.user.entity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaints extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;           //민원제목
    private String date;            //민원날짜
    private String complaintsName;  //민원인
    private String birthday;        //민원인 생년월일
    private String audioPath;
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;
    //private String tag;

    public static Complaints createComplaints(String title ,String date ,String complaintsName ,String birthday ,String audioPath , String content){
        return Complaints.builder().title(title).date(date).complaintsName(complaintsName).birthday(birthday).audioPath(audioPath).content(content).build();
    }
}
