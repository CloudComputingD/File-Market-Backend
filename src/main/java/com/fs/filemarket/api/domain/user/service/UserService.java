package com.fs.filemarket.api.domain.user.service;

import com.fs.filemarket.api.domain.user.Role;
import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.dto.UserDTO;
import com.fs.filemarket.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

        User user = User.builder()
                .email(userDTO.getEmail())
//                .login_id(userDTO.getLogin_id())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "로그인 되지 않았습니다."
            );
        }

        return (User) authentication.getPrincipal();
    }


}

