package com.sparta.spring_post.controller;

import com.sparta.spring_post.dto.LoginRequestDto;
import com.sparta.spring_post.dto.ResponseDto;
import com.sparta.spring_post.dto.SignupRequestDto;
import com.sparta.spring_post.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    // UserService 연결
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 로그인
//    @PostMapping("/login")
//    public ResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
//        return userService.login(loginRequestDto, httpServletResponse);
//    }

}
