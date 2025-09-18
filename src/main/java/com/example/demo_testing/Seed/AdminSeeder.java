package com.example.demo_testing.Seed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo_testing.Entity.User;
import com.example.demo_testing.Enum.Role;
import com.example.demo_testing.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = new ArrayList<User>(
            List.of(
                User.builder()
                    .email("admin_minh@gmail.com")
                    .name("ADMIN_MINH")
                    .role(Role.ADMIN)
                    .password("12345678_admin")
                .build(),
                User.builder()
                    .email("admin_phong@gmail.com")
                    .name("ADMIN_PHONG")
                    .role(Role.ADMIN)
                    .password("12345678_admin")
                .build()
            )
        );
        users.removeIf(user -> {
            if (userRepository.existsByEmail(user.getEmail())) {
                System.out.println("Admin '" + user.getEmail() + "' already exists. Skipping seed.");
                return true;
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return false;
        });
        userRepository.saveAll(users);
        System.out.println("Admin user seeding completed.");
    }
}
