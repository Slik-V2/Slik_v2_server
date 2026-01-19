package com.spring.slik_v2_server.domain.attendance.dto.response;

import java.time.LocalDate;
import java.util.List;

public record LiveAttendanceResponse(
		LocalDate date,
		List<StudentAttendanceResponse> students
) {

	public static LiveAttendanceResponse of(LocalDate date, List<StudentAttendanceResponse> students) {
		return new LiveAttendanceResponse(
				date,
				students);
	}
}