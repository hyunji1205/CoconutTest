package com.example.test.domain.user.service;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User signup(String username, String password, String nickname, String email, String phone) {
        User user = User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public User whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<User> opMember = findByUsername(username);

        if (opMember.isPresent()) return opMember.get();

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return signup(username, "", nickname, "",""); // 최초 로그인 시 딱 한번 실행
    }

    private Optional<User> findByUsername(String username) {
        return userRepository.findByusername(username);
    }
}
