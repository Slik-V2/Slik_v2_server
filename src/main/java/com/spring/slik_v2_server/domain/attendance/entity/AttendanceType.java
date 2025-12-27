package com.spring.slik_v2_server.domain.attendance.entity;

import lombok.Getter;

public enum AttendanceType {
    night_entry1(AttendanceTimeEnum.Night1Start), // 심자1 출석여부
    night_exit1(AttendanceTimeEnum.Night1End), // 심자1 복귀여부
    night_entry2(AttendanceTimeEnum.Night2Start), // 심자2 출석여부
    night_exit2(AttendanceTimeEnum.Night1End); // 심자2 복귀여부

    @Getter
    private final AttendanceTimeEnum defaultTime;

    AttendanceType(AttendanceTimeEnum defaultTime) {
        this.defaultTime = defaultTime;
    }
}