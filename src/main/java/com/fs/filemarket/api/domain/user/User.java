package com.fs.filemarket.api.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;

    // 제대로 동작하는지 test 필요
    @PrePersist
    public void there_is_no_nick() {
        this.nickname = this.nickname == null ? name : this.nickname;
    }

    // JPA 데이터베이스로 저장할 때 Enum 값을 어떤 형태로 저장할지 결정
    // 기본은 int 로 된 숫자이지만, DB 확인 시 편의성을 위해 문자열로 저장될 수 있도록 선언
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    @Column(nullable = false)
//    private String login_id;
//
//    @Column()
//    private String password;

    @Builder
    public User(Integer id, String name, String email, String nickname, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public User update(String nickname) {
        this.nickname = nickname;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


}