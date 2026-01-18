package com.spring.slik_v2_server.domain.attendance.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;

public record AttendanceTimeResponse(
		LocalDate today,
		LocalTime startTime,
		LocalTime endTime
) {

	public static AttendanceTimeResponse of(AttendanceTime attendanceTime) {
		return new AttendanceTimeResponse(attendanceTime.getToday(),
				attendanceTime.getStartTime(),
				attendanceTime.getEndTime());
	}

	public static List<AttendanceTimeResponse> fromList(List<AttendanceTime> attendanceTimes) {
		return attendanceTimes
				.stream()
				.map(AttendanceTimeResponse::of)
				.collect(Collectors.toList());
	}
}
