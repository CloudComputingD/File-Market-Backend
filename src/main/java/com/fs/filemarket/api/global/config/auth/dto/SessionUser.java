package com.fs.filemarket.api.global.config.auth.dto;

import com.fs.filemarket.api.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// 세션에 사용자 정보를 저장하기 위한 DTO
// 인증된 사용자 정보만 필요
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
