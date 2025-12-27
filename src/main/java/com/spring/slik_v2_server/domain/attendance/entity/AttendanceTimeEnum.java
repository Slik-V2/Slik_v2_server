package com.spring.slik_v2_server.domain.attendance.entity;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum AttendanceTimeEnum {

	Night1Start(LocalTime.of(21, 0), LocalTime.of(21, 15)),
	Night1End(LocalTime.of(21, 50), LocalTime.of(22, 15)),
	Night2Start(LocalTime.of(22, 40), LocalTime.of(23, 0)),
	Night2End(LocalTime.of(23, 50), LocalTime.of(23, 55));

	private final LocalTime defaultStartTime;
	private final LocalTime defaultEndTime;

	AttendanceTimeEnum(LocalTime defaultStartTime, LocalTime defaultEndTime) {
		this.defaultStartTime = defaultStartTime;
		this.defaultEndTime = defaultEndTime;
	}
}
