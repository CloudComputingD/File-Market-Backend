package com.fs.filemarket.api.domain.folder;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter @Setter
@ToString // 변수값을 리턴해주는 toString 메소드 자동생성
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자를 생성
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private LocalDateTime created_time;

    @Column
    private LocalDateTime modified_time;

    @Column
    private LocalDateTime deleted_time;

    @Column(nullable = false)
    private boolean favorite;

    @Column(nullable = false)
    private boolean trash;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false);
    private User user;

    @OneToMany(mappedBy = "folder", cascade = {CascadeType.REMOVE})
    private Set<FileFolder> files = new HashSet<>();

}
