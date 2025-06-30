package com.example.index.fantastic_app.auth.config;

import com.example.index.fantastic_app.auth.domain.User;
import com.example.index.fantastic_app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final PasswordEncoder encoder;
    private final UserRepository repo;

    @Bean
    CommandLineRunner initUsers() {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new User(null,"user1", encoder.encode("pass1"), "ROLE_USER"));
                repo.save(new User(null,"admin", encoder.encode("admin123"), "ROLE_ADMIN"));
            }
        };
    }
}
