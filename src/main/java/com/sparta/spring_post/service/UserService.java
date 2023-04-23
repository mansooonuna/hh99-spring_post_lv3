package com.sparta.spring_post.service;

import com.sparta.spring_post.dto.LoginRequestDto;
import com.sparta.spring_post.dto.ResponseDto;
import com.sparta.spring_post.dto.SignupRequestDto;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.jwt.JwtUtil;
import com.sparta.spring_post.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 아이디 형식 확인
        if (!Pattern.matches("^[a-z0-9]{4,10}$", username)) {
            return ResponseDto.setFailed("형식에 맞지 않는 아이디 입니다.");
        }

        // 비밀번호 형식 확인
        if (!Pattern.matches("^[a-zA-Z0-9]{8,15}$", password)) {
            return ResponseDto.setFailed("형식에 맞지 않는 비밀번호 입니다.");
        }

        // 회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return ResponseDto.setFailed("중복된 사용자입니다.");
        }

        Users users = new Users(username, password);
        userRepository.save(users);
        return ResponseDto.setSuccess("회원가입 성공", null);
    }

    @Transactional(readOnly = true)
    public ResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 아이디 확인
        Users users = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );

        // 비밀번호 확인
        if (!users.getPassword().equals(password)) {
            return ResponseDto.setFailed("일치하지 않는 비밀번호 입니다.");
        }

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(users.getUsername(), users.getRole()));
        return ResponseDto.setSuccess("로그인 성공", null);
    }

}
