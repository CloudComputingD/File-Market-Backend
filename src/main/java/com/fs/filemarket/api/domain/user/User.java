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

//    @Column(nullable = false)
//    private String login_id;
//
//    @Column()
//    private String password;

    @Builder
    public User(Integer id, String name, String email, String nickname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

    public User update(String nickname) {
        this.nickname = nickname;

        return this;
    }


}