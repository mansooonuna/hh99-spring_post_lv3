package com.sparta.spring_post.service;

import com.sparta.spring_post.dto.CommentRequestDto;
import com.sparta.spring_post.dto.PostRequestDto;
import com.sparta.spring_post.dto.ResponseDto;
import com.sparta.spring_post.entity.Comment;
import com.sparta.spring_post.entity.Post;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.jwt.JwtUtil;
import com.sparta.spring_post.repository.CommentRepository;
import com.sparta.spring_post.repository.PostRepository;
import com.sparta.spring_post.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    // 댓글 등록
    @Transactional
    public ResponseDto<Comment> addComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        Users user = checkJwtToken(httpServletRequest);

        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );


        Comment comment = new Comment(user, commentRequestDto);
        commentRepository.save(comment);
        return ResponseDto.setSuccess("댓글이 등록되었습니다.", comment);
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<Comment> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        Users user = checkJwtToken(httpServletRequest);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        comment.update(commentRequestDto);
        return ResponseDto.setSuccess("댓글이 수정되었습니다.", comment);

    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<Comment> deleteComment(Long id,  HttpServletRequest httpServletRequest) {
        Users user = checkJwtToken(httpServletRequest);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        commentRepository.delete(comment);
        return ResponseDto.setSuccess("댓글 삭제 성공", comment);

    }


    // 토큰 체크
    public Users checkJwtToken(HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 접근 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            Users user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }
}