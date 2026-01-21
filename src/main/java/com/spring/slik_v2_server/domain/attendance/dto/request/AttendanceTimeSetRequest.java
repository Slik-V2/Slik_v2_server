package com.spring.slik_v2_server.domain.attendance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record AttendanceTimeSetRequest(
		@JsonFormat(pattern = "HH:mm")
		LocalTime session1_1Start,
		@JsonFormat(pattern = "HH:mm")
		LocalTime session1_1End,

		@JsonFormat(pattern = "HH:mm")
		LocalTime session1_2Start,
		@JsonFormat(pattern = "HH:mm")
		LocalTime session1_2End,

		@JsonFormat(pattern = "HH:mm")
		LocalTime session2_1Start,
		@JsonFormat(pattern = "HH:mm")
		LocalTime session2_1End,

		@JsonFormat(pattern = "HH:mm")
		LocalTime session2_2Start,
		@JsonFormat(pattern = "HH:mm")
		LocalTime session2_2End
) {
}
