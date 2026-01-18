package com.spring.slik_v2_server.domain.attendance.repository;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceSetRepository extends JpaRepository<AttendanceTimeSet, Long> {

	Optional<AttendanceTimeSet> findByToday(LocalDate today);
}
