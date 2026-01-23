package com.spring.slik_v2_server.domain.attendance.repository;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface  AttendanceRepository extends JpaRepository<AttendanceTime, Long> {

	List<AttendanceTime> findAllByFingerPrintAndTodayBetween(FingerPrint fingerPrint, LocalDate todayAfter, LocalDate todayBefore);

	List<AttendanceTime> findAllByToday(LocalDate date);

	Optional<AttendanceTime> findByFingerPrint_IdAndToday(String fingerPrintId, LocalDate today);

	@Query("SELECT a FROM AttendanceTime a WHERE a.student = :student AND (a.s1Status = :status OR a.s2Status = :status) AND a.today BETWEEN :startDate AND :endDate")
	List<AttendanceTime> findAllByStudentAndAttendanceStatusIsNoneAndTodayBetween(
			@Param("student") Student student,
			@Param("status") AttendanceStatus status,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	List<AttendanceTime> findAllByStudentAndTodayBetween(Student student, LocalDate startDate, LocalDate endDate);
}
