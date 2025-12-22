package com.spring.slik_v2_server.domain.teacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.TEACHER;

    public void setPassword(String password) {
        this.password = password;
    }
}