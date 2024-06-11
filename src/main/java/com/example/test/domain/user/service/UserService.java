package com.example.test.domain.user.service;

import com.example.test.domain.user.controller.UserController;
import com.example.test.domain.user.entity.User;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.domain.user.controller.UserController.EditProfileForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

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
        Optional<User> opUser = userRepository.findByUsername(username);

        if (opUser.isPresent()) return opUser.get();

        return signup(username, "", nickname, "", ""); // 최초 로그인 시 딱 한 번 실행
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateProfile(EditProfileForm form, MultipartFile profileImageFile) throws IOException {
        User user = getCurrentUser();
        user.setNickname(form.getNickname());
        user.setPassword(form.getPassword());
        user.setPhone(form.getPhone());

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String profileImagePath = saveProfileImage(profileImageFile);
            user.setProfileImagePath(profileImagePath);
        }

        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        userRepository.save(user);
    }

    private String saveProfileImage(MultipartFile profileImageFile) throws IOException {
        String uploadDir = "/Users/jaemin/work/upload/";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String originalFilename = profileImageFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        File destinationFile = new File(uploadDir + newFilename);
        profileImageFile.transferTo(destinationFile);

        return  newFilename;
    }


    private Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
