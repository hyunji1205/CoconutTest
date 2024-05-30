package com.example.test.domain.user.controller;

import com.example.test.domain.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignForm signForm) {

        userService.signup(signForm.getUsername(), signForm.getPassword(), signForm.getNickname(), signForm.getEmail(), signForm.getPhone());

        return "redirect:/user/login";
    }
    @Getter
    @Setter
    @ToString
    public static class SignForm {
        @NotBlank
        @Length(min = 3)
        private String username;

        @NotBlank
        @Length(min = 4)
        private String password;

        @NotBlank
        @Length(min = 4)
        private String password_confirm;

        @NotBlank
        @Length(min = 3)
        private String nickname;

        @NotBlank
        @Length(min = 4)
        private String email;

        @NotBlank
        @Length(min = 4)
        private String phone;
    }
}
