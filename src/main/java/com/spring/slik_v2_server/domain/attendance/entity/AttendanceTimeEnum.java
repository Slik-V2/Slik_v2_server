package com.spring.slik_v2_server.domain.attendance.entity;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum AttendanceTimeEnum {

	session1_1Start(LocalTime.of(21, 0)),
	session1_1End(LocalTime.of(21, 15)),
	session1_2Start(LocalTime.of(21, 50)),
	session1_2End(LocalTime.of(22, 15)),
	session2_1Start(LocalTime.of(22, 40)),
	session2_1End(LocalTime.of(23, 0)),
	session2_2Start(LocalTime.of(23, 50)),
	session2_2End(LocalTime.of(23, 59));

	private final LocalTime defaultTime;

	AttendanceTimeEnum(LocalTime defaultTime) {
		this.defaultTime = defaultTime;
	}
}
