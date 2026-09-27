package com.platform.opportunity.service;

import com.platform.opportunity.dto.AuthResponse;
import com.platform.opportunity.dto.LoginRequest;
import com.platform.opportunity.dto.RegisterRequest;
import com.platform.opportunity.model.Role;
import com.platform.opportunity.model.StudentProfile;
import com.platform.opportunity.model.User;
import com.platform.opportunity.repository.StudentProfileRepository;
import com.platform.opportunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse registerStudent(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        // 1. Create Base User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        user = userRepository.save(user);

        // 2. Create Student Profile (linked by ID)
        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        studentProfileRepository.save(profile);

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                profile.getFullName(),
                user.getRole(),
                "Registration successful"
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String fullName = "Admin";
        if (user.getRole() == Role.STUDENT) {
            Optional<StudentProfile> profile = studentProfileRepository.findById(user.getId());
            if (profile.isPresent()) {
                fullName = profile.get().getFullName();
            }
        }

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                fullName,
                user.getRole(),
                "Login successful"
        );
    }
}
