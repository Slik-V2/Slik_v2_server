package com.spring.slik_v2_server.domain.attendance.entity;

import lombok.Getter;

public enum AttendanceType {
    session1_1Start(AttendanceTimeEnum.session1_1Start),
    session1_1End(AttendanceTimeEnum.session1_1End),
    session1_2Start(AttendanceTimeEnum.session1_2Start),
    session1_2End(AttendanceTimeEnum.session1_2End),
    session2_1Start(AttendanceTimeEnum.session2_1Start),
    session2_1End(AttendanceTimeEnum.session2_1End),
    session2_2Start(AttendanceTimeEnum.session2_2Start),
    session2_2End(AttendanceTimeEnum.session2_2End);

    @Getter
    private final AttendanceTimeEnum defaultTime;

    AttendanceType(AttendanceTimeEnum defaultTime) {
        this.defaultTime = defaultTime;
    }
    }