package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeEnum;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.device.dto.request.UpdateDeviceRequest;
import com.spring.slik_v2_server.domain.device.dto.request.VerifyDeviceRequest;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final AttendanceRepository attendanceRepository;
    private final FingerPrintRepository fingerPrintRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ApiResponse<String> verifyAttendance(VerifyDeviceRequest request) {
        LocalTime time = LocalTime.now();
        AttendanceType type = determineAttendanceType(time);

        FingerPrint fingerPrint = fingerPrintRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

        AttendanceTime attendance = attendanceRepository.findByFingerPrintAndTodayAndType(fingerPrint, LocalDate.now(), type)
                .map(attendanceTime -> {
                    attendanceTime.setEndTime(LocalTime.now());
                    return attendanceRepository.save(attendanceTime);
                }).orElseGet(() -> attendanceRepository.save(AttendanceTime.builder()
                                .fingerPrint(fingerPrint)
                                .today(LocalDate.now())
                                .startTime(LocalTime.now())
                                .type(type)
                        .build())
                );

        AttendanceTimeResponse response = AttendanceTimeResponse.of(attendance);
        messagingTemplate.convertAndSend("/topic/device/" + request.device_id(), response);

        return ApiResponse.ok("인증되었습니다.");
    }

    public ApiResponse<List<AttendanceTimeResponse>> findAttendanceStatus(String id) {
        LocalDate today = LocalDate.now();

        FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(id)
                .orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

        List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByFingerPrintAndTodayBetween(fingerPrint, today, today);
        List<AttendanceTimeResponse> attendanceStatus = AttendanceTimeResponse.fromList(attendanceTimes);
        return ApiResponse.ok(attendanceStatus);
    }

    public ApiResponse<String> updateAttendanceStatus(UpdateDeviceRequest request) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        AttendanceType type = determineAttendanceType(now);

        FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(request.student_id())
                .orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

        attendanceRepository.findByFingerPrintAndTodayAndType(fingerPrint, today, type)
                .map(attendanceTime -> {
                    attendanceTime.setType(type);
                    return attendanceRepository.save(attendanceTime);
                })
                .orElseGet(() -> attendanceRepository.save(AttendanceTime.builder()
                        .today(today)
                        .fingerPrint(fingerPrint)
                        .type(request.new_status())
                        .build()));

        return ApiResponse.ok("변경되었습니다.");
    }

    private AttendanceType determineAttendanceType(LocalTime now) {
        for (AttendanceType type : AttendanceType.values()) {
            Optional<AttendanceTime> scheduleOpt = attendanceRepository.findByType(type);

            if (scheduleOpt.isPresent()) {
                AttendanceTime schedule = scheduleOpt.get();
                LocalTime startTime = schedule.getStartTime();
                LocalTime endTime = schedule.getEndTime();

                if (now.isAfter(startTime.minusSeconds(1)) &&
                        now.isBefore(endTime.plusSeconds(1))) {
                    return type;
                }
            } else {
                AttendanceTimeEnum defaultTime = type.getDefaultTime();
                LocalTime defaultStart = defaultTime.getDefaultStartTime();
                LocalTime defaultEnd = defaultTime.getDefaultEndTime();

                if (now.isAfter(defaultStart.minusSeconds(1)) &&
                        now.isBefore(defaultEnd.plusSeconds(1))) {
                    return type;
                }
            }
        }
        return null;
    }
}
