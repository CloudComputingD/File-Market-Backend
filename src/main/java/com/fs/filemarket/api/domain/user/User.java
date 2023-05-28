package com.fs.filemarket.api.domain.user;

import com.fs.filemarket.api.domain.file.File;
import com.fs.filemarket.api.domain.folder.FileFolder;
import com.fs.filemarket.api.domain.folder.Folder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
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

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private Set<Folder> folders = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private Set<File> files = new HashSet<>();

    // JPA 데이터베이스로 저장할 때 Enum 값을 어떤 형태로 저장할지 결정
    // 기본은 int 로 된 숫자이지만, DB 확인 시 편의성을 위해 문자열로 저장될 수 있도록 선언
    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;   // Google, Kakao

    private String socialId;         // 로그인 한 소셜 타입의 식별자 값(일반 로그인의 경우, null)

    private String refreshToken;

    @Builder
    public User(Integer id, String name, String email, String login_id, String password, Role role, SocialType socialType, String socialId ) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.login_id = login_id;
            this.password = password;
            this.role = role;
            this.socialType = socialType;
            this.socialId = socialId;
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