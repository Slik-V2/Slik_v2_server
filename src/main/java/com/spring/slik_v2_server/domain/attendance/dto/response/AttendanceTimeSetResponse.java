package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;

import java.time.LocalTime;

public record AttendanceTimeSetResponse (
		LocalTime session1_1Start,
		LocalTime session1_1End,

		LocalTime session1_2Start,
		LocalTime session1_2End,

		LocalTime session2_1Start,
		LocalTime session2_1End,

		LocalTime session2_2Start,
		LocalTime session2_2End
) {

	public static AttendanceTimeSetResponse of(AttendanceTimeSet attendanceTimeSet) {
		return new AttendanceTimeSetResponse(
				attendanceTimeSet.getSession1_1Start(),
				attendanceTimeSet.getSession1_1End(),
				attendanceTimeSet.getSession1_2Start(),
				attendanceTimeSet.getSession1_2End(),
				attendanceTimeSet.getSession2_1Start(),
				attendanceTimeSet.getSession2_1End(),
				attendanceTimeSet.getSession2_2Start(),
				attendanceTimeSet.getSession2_2End()
		);
	}
}
