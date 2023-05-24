package com.fs.filemarket.api.domain.user.repository;

import com.fs.filemarket.api.domain.user.SocialType;
import com.fs.filemarket.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 데이터베이스에 존재하는 이메일인지 확인하는 method
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginId(String login_id);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
