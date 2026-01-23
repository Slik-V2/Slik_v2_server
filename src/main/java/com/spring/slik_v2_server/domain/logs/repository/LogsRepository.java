package com.spring.slik_v2_server.domain.logs.repository;

import com.spring.slik_v2_server.domain.logs.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Logs, Long> {
    List<Logs> findAllByStudent_StudentId(String studentId);
}
