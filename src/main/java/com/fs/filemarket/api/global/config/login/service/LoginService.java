package com.fs.filemarket.api.global.config.login.service;

import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // DaoAuthenticationProvider 가 설정해준 email 을 가진 유저를 찾음
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin_id())    // 원래 user.getEmail()
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}