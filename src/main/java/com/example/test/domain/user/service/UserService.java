package com.example.test.domain.user.service;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;

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
    private Optional<User> findByUsername(String username) {
        return userRepository.findByusername(username);
    }
}