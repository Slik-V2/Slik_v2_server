package com.spring.slik_v2_server.domain.teacher.repository;

import com.spring.slik_v2_server.domain.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUsernameAndIsActiveTrue(String username);

    Optional<Teacher> findByUsername(String username);
}
