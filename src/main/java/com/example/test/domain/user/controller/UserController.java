package com.example.test.domain.user.controller;

import com.example.test.domain.user.entity.User;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profilePage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/edit")
    public String editProfilePage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/edit")
    public String editProfile(@Valid EditProfileForm form, @RequestParam("profileImageFile") MultipartFile profileImageFile) throws IOException {
        userService.updateProfile(form, profileImageFile);
        return "redirect:/user/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts")
    public String myPostsPage(Model model) {
        // 사용자의 게시물을 모델에 추가
        return "user/posts";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/scrap")
    public String myScrapPage(Model model) {
        // 사용자의 스크랩 목록을 모델에 추가
        return "user/scrap";
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

    @Getter
    @Setter
    @ToString
    public static class EditProfileForm {

        @NotBlank
        private String nickname;

        // 비밀번호가 비어도 수정할 수 있도록 변경
        private String password;

        @NotBlank
        private String phone;

        private MultipartFile profileImageFile;  // 추가
    }
}
