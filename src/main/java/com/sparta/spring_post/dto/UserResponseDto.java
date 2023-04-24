package com.sparta.spring_post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "set")
public class UserResponseDto<D> {
    private String message;
    private int statusCode;
//    private D data;

    public static <D> UserResponseDto<D> setSuccess(String message) {
        return UserResponseDto.set(message, 200);
    }

    public static <D> UserResponseDto<D> setFailed(String message) {
        return UserResponseDto.set(message, 400);
    }

}
