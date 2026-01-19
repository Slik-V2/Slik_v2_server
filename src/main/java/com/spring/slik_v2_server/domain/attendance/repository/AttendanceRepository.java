package com.spring.slik_v2_server.domain.attendance.repository;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface  AttendanceRepository extends JpaRepository<AttendanceTime, Long> {

	List<AttendanceTime> findAllByFingerPrintAndTodayBetween(FingerPrint fingerPrint, LocalDate todayAfter, LocalDate todayBefore);

	List<AttendanceTime> findAllByToday(LocalDate date);

	Optional<AttendanceTime> findByFingerPrint_IdAndToday(String fingerPrintId, LocalDate today);

	Optional<AttendanceTime> findByFingerPrintAndTodayAndType(FingerPrint fingerPrint, LocalDate today, AttendanceType type);
}
