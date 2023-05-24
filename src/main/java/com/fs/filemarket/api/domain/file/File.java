package com.fs.filemarket.api.domain.file;

import com.fs.filemarket.api.domain.folder.FileFolder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder // 클래스에 대한 빌더 패턴을 자동으로 생성한다.
@Getter
@Setter //Lombok어노테이션으로, 클래스의 필드에 대한 getter와 setter메소드를 자동으로 생성
@ToString // 변수값을 리턴해주는 toString 메소드 자동생성
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자를 생성
@Entity // 클래스를 엔티티로 표시하여 관계형 데이터베이스의 테이블을 나타낸다
@EntityListeners(AuditingEntityListener.class)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column //해당 필드가 데이터베이스 테이블 컬럼에 매핑되는 것을 나타냄
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime created_time; // 날짜와 시간을 나타내는 불변객체이다.
    // 날짜와 시간을 조작하고 형식화하는 다양한 메서드를 제공하여 시간 기반 작업을
    @Column
    private LocalDateTime modified_time;

    @Column
    private LocalDateTime deleted_time;

    @Column(nullable = false) // 해당컬럼은 null값을 허용하지 않는다.
    private boolean favorite;

    @Column(nullable = false)
    private boolean trash;

    @OneToMany(mappedBy ="file")
    private Set<FileFolder> folders = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false);
    private User user;
}
