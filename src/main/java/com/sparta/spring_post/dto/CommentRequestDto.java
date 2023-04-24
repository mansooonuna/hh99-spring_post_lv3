package com.sparta.spring_post.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String content;

}
