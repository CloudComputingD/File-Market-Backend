package com.fs.filemarket.api.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column(nullable = false, unique = true)
    private String login_id;

    @Column(nullable = false)
    private String password;

    private String refreshToken;

    // JPA 데이터베이스로 저장할 때 Enum 값을 어떤 형태로 저장할지 결정
    // 기본은 int 로 된 숫자이지만, DB 확인 시 편의성을 위해 문자열로 저장될 수 있도록 선언
    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;   // Google, Kakao

    private String socialId;         // 로그인 한 소셜 타입의 식별자 값(일반 로그인의 경우, null)


    @Builder
    public User(Integer id, String name, String email, String login_id, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login_id = login_id;
        this.password = password;
        this.role = role;
    }

    public User update(String name) {
        this.name = name;

        return this;
    }


    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }


}