package com.sparta.spring_post.controller;

import com.sparta.spring_post.dto.PostRequestDto;
import com.sparta.spring_post.dto.ResponseDto;
import com.sparta.spring_post.entity.Post;
import com.sparta.spring_post.service.PostService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    // PostService 연결
    private final PostService postService;

    // 목록 조회
    @GetMapping("/posts")
    public ResponseDto<List<Post>> getAllPosts() {
        return postService.getAllPosts();
    }

    // 상세 조회
    @GetMapping("/posts/{id}")
    public ResponseDto<Post> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 추가
    @PostMapping("/post")
    public ResponseDto<Post> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.createPost(postRequestDto, httpServletRequest);
    }

    // 수정
    @PutMapping("/post/{id}")
    public ResponseDto<Post> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.updatePost(id, postRequestDto, httpServletRequest);
    }

    // 삭제
    @DeleteMapping("/post/{id}")
    public ResponseDto deletePost(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return postService.deletePost(id, httpServletRequest);
    }

}
