package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record AbsencesResponse(
        LocalDate date,
        AttendanceStatus s1Status,
        AttendanceStatus s2Status
) {
    public static AbsencesResponse of(AttendanceTime attendanceTime) {
        return new AbsencesResponse(
                attendanceTime.getToday(),
                attendanceTime.getS1Status(),
                attendanceTime.getS2Status()
        );
    }

    public static List<AbsencesResponse> formlist(List<AttendanceTime> attendanceTimes) {
        return attendanceTimes.stream().map(AbsencesResponse::of).collect(Collectors.toList());
    }
}
