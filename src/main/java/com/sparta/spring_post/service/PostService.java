package com.sparta.spring_post.service;

import com.sparta.spring_post.dto.PostRequestDto;
import com.sparta.spring_post.dto.ResponseDto;
import com.sparta.spring_post.entity.Post;
import com.sparta.spring_post.entity.RoleType;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.jwt.JwtUtil;
import com.sparta.spring_post.repository.PostRepository;
import com.sparta.spring_post.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    // PostRepository 연결
    private final PostRepository postRepository;
    // UserRepository 연결
    private final UserRepository userRepository;
    // JwtUtil 연결
    private final JwtUtil jwtUtil;

    // 목록 조회
    @Transactional(readOnly = true)
    public ResponseDto<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return ResponseDto.setSuccess("게시물 목록 조회 성공!", posts);
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public ResponseDto<Post> getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(id + "번 게시물이 존재하지 않습니다.")
        );
        return ResponseDto.setSuccess(id + "번 게시물 조회 성공!", post);
    }

    // 추가
    @Transactional
    public ResponseDto<Post> createPost(PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);

        if (token == null) {
            return ResponseDto.setFailed("토큰이 없습니다.");
        }

        try {
            jwtUtil.validateToken(token);
        } catch (Exception e) {
            return ResponseDto.setFailed("유효한 토큰이 없습니다.");
        }

        Users user = userRepository.findByUsername(postRequestDto.getUsername()).orElseThrow();

        Post post = new Post(user, postRequestDto);
        postRepository.save(post);
        return ResponseDto.setSuccess("게시물 작성 성공!", post);
    }

    // 수정
    @Transactional
    public ResponseDto<Post> updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(id + "번 게시물이 없습니다.")
        );

        if (token == null) {
            return ResponseDto.setFailed("토큰이 없습니다.");
        }

        try {
            jwtUtil.validateToken(token);
        } catch (Exception e) {
            return ResponseDto.setFailed("유효한 토큰이 없습니다.");
        }

        claims = jwtUtil.getUserInfoFromToken(token);

        Users user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        if (post.getUsers().getUsername().equals(claims.getSubject()) || user.getRole().equals(RoleType.ADMIN)) {
            post.update(postRequestDto);
            return ResponseDto.setSuccess(id + "번 게시물 수정 성공!", post);
        } else {
            return ResponseDto.setFailed(id + "번 게시물을 수정할 권한이 없습니다.");
        }
    }

    // 삭제
    @Transactional
    public ResponseDto deletePost(Long id, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(id + "번 게시물이 없습니다.")
        );

        if (token == null) {
            return ResponseDto.setFailed("토큰이 없습니다.");
        }

        try {
            jwtUtil.validateToken(token);
        } catch (Exception e) {
            return ResponseDto.setFailed("유효한 토큰이 없습니다.");
        }

        claims = jwtUtil.getUserInfoFromToken(token);

        Users user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        if (post.getUsers().getUsername().equals(claims.getSubject()) || user.getRole().equals(RoleType.ADMIN)) {
            postRepository.deleteById(id);
            return ResponseDto.setSuccess(id + "번 게시물 삭제 성공!", null);
        } else {
            return ResponseDto.setFailed(id + "번 게시물을 삭제할 권한이 없습니다.");
        }
    }

}
