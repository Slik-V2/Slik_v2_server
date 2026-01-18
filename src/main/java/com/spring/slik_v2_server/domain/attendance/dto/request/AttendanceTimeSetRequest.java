package com.spring.slik_v2_server.domain.attendance.dto.request;

import java.time.LocalTime;

public record AttendanceTimeSetRequest(
		LocalTime session1_1Start,
		LocalTime session1_1End,

		LocalTime session1_2Start,
		LocalTime session1_2End,

		LocalTime session2_1Start,
		LocalTime session2_1End,

		LocalTime session2_2Start,
		LocalTime session2_2End
) {
}
