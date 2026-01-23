package com.spring.slik_v2_server.domain.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.slik_v2_server.domain.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByFingerPrint_Id(Long id);
    Optional<Student> findByStudentId(String studentId);

    List<Student> findAll();
}