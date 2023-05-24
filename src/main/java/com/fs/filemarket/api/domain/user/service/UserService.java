package com.fs.filemarket.api.domain.user.service;

import com.fs.filemarket.api.domain.user.Role;
import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.dto.UserDTO;
import com.fs.filemarket.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
// 자체 로그인, 회원가입 시 사용하는 화원 가입 API
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(UserDTO userDTO) throws Exception{

        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(userRepository.findByLoginId(userDTO.getLogin_id()).isPresent()) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .login_id(userDTO.getLogin_id())
                .password(userDTO.getPassword())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

    }



}

