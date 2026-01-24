package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.dodam.entity.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record AbsencesResponse(
        LocalDate date,
        String session,
        String period
) {
    public static List<AbsencesResponse> fromList(List<AttendanceTime> attendanceTimes) {
        List<AbsencesResponse> responses = new ArrayList<>();
        for (AttendanceTime at : attendanceTimes) {
            if (at.getS1Status() == AttendanceStatus.NONE) {
                responses.add(new AbsencesResponse(at.getToday(), "s1", "심자 1"));
            }
            if (at.getType() == Type.NIGHT_STUDY_2 && at.getS2Status() == AttendanceStatus.NONE) {
                responses.add(new AbsencesResponse(at.getToday(), "s2", "심자 2"));
            }
        }
        return responses;
    }
}
