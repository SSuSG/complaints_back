package com.toyproject.complaints.global.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ListResponseResult<T> extends ResponseResult{

    private List<T> data;

    public ListResponseResult(int statusCode, String messages, String developerMessage, LocalDateTime timestamp , List<T> data) {
        super(statusCode, messages, developerMessage, timestamp);
        this.data = data;
    }
}
