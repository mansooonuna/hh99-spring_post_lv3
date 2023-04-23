package com.sparta.spring_post.service;

import com.sparta.spring_post.dto.ResponseDto;
import com.sparta.spring_post.dto.SignupRequestDto;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public ResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 아이디 형식
        if (!Pattern.matches("^[a-z0-9]{4,10}$", username)) {
            return ResponseDto.setFailed("형식에 맞지 않는 아이디 입니다.");
        }

        // 비밀번호 형식
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

}
