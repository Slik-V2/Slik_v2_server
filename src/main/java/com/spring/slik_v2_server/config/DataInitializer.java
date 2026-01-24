package com.spring.slik_v2_server.config;

import com.spring.slik_v2_server.domain.teacher.entity.Role;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;
import com.spring.slik_v2_server.domain.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    // Default admin credentials (should ideally come from environment variables)
    @Value("${DEFAULT_ADMIN_ID:admin}")
    private String defaultAdminId;
    @Value("${DEFAULT_ADMIN_PASSWORD:admin1234}")
    private String defaultAdminPassword;
    @Value("${DEFAULT_ADMIN_NAME:기본 관리자}")
    private String defaultAdminName;
    @Value("${DEFAULT_ADMIN_EMAIL:admin@example.com}")
    private String defaultAdminEmail;

    @Bean
    public CommandLineRunner initAdminAccount() {
        return args -> {
            if (teacherRepository.findByUsername(defaultAdminId).isEmpty()) {
                Teacher admin = Teacher.builder()
                        .username(defaultAdminId)
                        .password(passwordEncoder.encode(defaultAdminPassword))
                        .name(defaultAdminName)
                        .email(defaultAdminEmail)
                        .role(Role.ADMIN)
                        .isActive(true)
                        .build();
                teacherRepository.save(admin);
                System.out.println("Default admin account created: " + defaultAdminId);
            } else {
                System.out.println("Default admin account already exists: " + defaultAdminId);
            }
        };
    }
}
