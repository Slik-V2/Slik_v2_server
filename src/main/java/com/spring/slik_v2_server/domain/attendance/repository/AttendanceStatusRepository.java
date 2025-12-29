package com.spring.slik_v2_server.domain.attendance.repository;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus, Long> {
    List<AttendanceStatus> findAllByFingerPrint_StudentIdAndDateBetween(String fingerPrintStudentId, LocalDateTime start, LocalDateTime end);
}
