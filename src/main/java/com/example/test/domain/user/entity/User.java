package com.example.test.domain.user.entity;

import com.example.test.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends BaseEntity {
    @Comment("유저 아이디")
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)

    private String email;

    private String phone;

    private String profileImagePath;  // 이미지 파일 경로

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
