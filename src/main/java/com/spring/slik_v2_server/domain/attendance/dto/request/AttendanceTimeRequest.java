package com.spring.slik_v2_server.domain.attendance.dto.request;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record AttendanceTimeRequest (
		@Enumerated(EnumType.STRING)
		@NotBlank AttendanceType attendanceType,

		LocalTime startTime,

		LocalTime endTime
) {
}
