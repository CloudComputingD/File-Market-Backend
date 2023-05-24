package com.fs.filemarket.api.domain.user.service;

import com.fs.filemarket.api.domain.user.Role;
import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.dto.UserDTO;

// 자체 로그인, 회원가입 시 사용하는 화원 가입 API
public interface UserService {

    void join(UserDTO userDTO) throws Exception;

    default User ToEntity(UserDTO userDTO) {
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .login_id(userDTO.getLogin_id())
                .password(userDTO.getPassword())
                .role(Role.USER)
                .build();

        return user;
    }


}

