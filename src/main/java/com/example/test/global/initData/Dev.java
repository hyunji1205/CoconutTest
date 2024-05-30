package com.example.test.global.initData;


import com.example.test.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class Dev {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner init(UserService userService) {
        return args -> {
            userService.signup("test", "1234", "test", "test@test.com", "01012341234");
            userService.signup("user", "1234", "user", "user@test.com", "01012341234");
            userService.signup("esong", "1234", "esong", "esong@test.com", "01012341234");

        };
    }
}
