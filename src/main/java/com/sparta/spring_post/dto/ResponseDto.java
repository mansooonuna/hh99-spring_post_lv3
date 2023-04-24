package com.sparta.spring_post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "set")
public class ResponseDto<D> {
    private String message;
    private int statusCode;
    private D data;

    public static <D> ResponseDto<D> setSuccess(String message, D data) {
        return ResponseDto.set(message, 200, data);
    }

    public static <D> ResponseDto<D> setFailed(String message) {
        return ResponseDto.set(message, 400, null);
    }

}
