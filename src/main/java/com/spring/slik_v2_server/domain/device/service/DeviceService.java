package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.exception.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceSetRepository;
import com.spring.slik_v2_server.domain.device.dto.request.UpdateDeviceRequest;
import com.spring.slik_v2_server.domain.device.dto.request.VerifyDeviceRequest;
import com.spring.slik_v2_server.domain.dodam.entity.Type;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.domain.student.exception.StudentStatus;
import com.spring.slik_v2_server.domain.student.repository.StudentRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceSetRepository attendanceSetRepository;
    private final FingerPrintRepository fingerPrintRepository;
    private final StudentRepository studentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void verifyAttendance(VerifyDeviceRequest request) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        try {
            Student student = studentRepository.findByFingerPrint_Id(request.id())
                    .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

            AttendanceTimeSet timeSet = attendanceSetRepository.findByToday(today)
                    .orElseThrow(() -> new ApplicationException(AttendanceStatus.NOT_IN_ATTENDANCE_TIME));

            AttendanceTime attendanceTime = attendanceRepository.findByFingerPrint_IdAndToday(request.id(), today)
                    .orElseThrow(() -> new ApplicationException(AttendanceStatus.NOT_IN_ATTENDANCE_TIME));

            java.util.Optional<AttendanceType> currentSessionTypeOpt = getTypeByTimeRange(now, timeSet);

            if (currentSessionTypeOpt.isEmpty()) {
                messagingTemplate.convertAndSend("/topic/device/" + request.device_id(), (Object) Map.of(
                        "status", "FAILED",
                        "message", "출석 가능 시간이 아닙니다.",
                        "timestamp", LocalTime.now().toString()
                ));
                return;
            }

            AttendanceType currentSessionType = currentSessionTypeOpt.get();

            if (currentSessionType.name().contains("session2") && attendanceTime.getType() == Type.NIGHT_STUDY_1) {
                messagingTemplate.convertAndSend("/topic/device/" + request.device_id(), (Object) Map.of(
                        "status", "FAILED",
                        "message", "심자1 대상은 심자2에 참여할 수 없습니다.",
                        "timestamp", LocalTime.now().toString()
                ));
                return;
            }

            LocalTime s1OutTimeBeforeUpdate = attendanceTime.getS1OutTime();
            attendanceTime.updateAttendanceTime(now, timeSet);
            LocalTime s1OutTimeAfterUpdate = attendanceTime.getS1OutTime();

            if (s1OutTimeBeforeUpdate == null && s1OutTimeAfterUpdate != null &&
                attendanceTime.getType() == Type.NIGHT_STUDY_1) {
                student.increaseAttendanceDate();
            }

            attendanceRepository.save(attendanceTime);
            studentRepository.save(student);

            messagingTemplate.convertAndSend("/topic/device/" + request.device_id(), (Object) Map.of(
                    "status", "SUCCESS",
                    "name", student.getName(),
                    "timestamp", LocalTime.now().toString()
            ));
        } catch (ApplicationException e) {
            messagingTemplate.convertAndSend("/topic/device/" + request.device_id(), (Object) Map.of(
                    "status", "FAILED",
                    "message", e.getMessage(),
                    "timestamp", LocalTime.now().toString()
            ));
        }
    }


    private java.util.Optional<AttendanceType> getTypeByTimeRange(LocalTime now, AttendanceTimeSet timeSet) {
        return java.util.Arrays.stream(AttendanceType.values())
                .filter(type -> isTimeInRange(now, type, timeSet))
                .findFirst();
    }

    private boolean isTimeInRange(LocalTime now, AttendanceType type, AttendanceTimeSet timeSet) {
        return switch (type) {
            case session1_1Start, session1_1End -> isInRange(now, timeSet.getSession1_1Start(), timeSet.getSession1_1End());
            case session1_2Start, session1_2End -> isInRange(now, timeSet.getSession1_2Start(), timeSet.getSession1_2End());
            case session2_1Start, session2_1End -> isInRange(now, timeSet.getSession2_1Start(), timeSet.getSession2_1End());
            case session2_2Start, session2_2End -> isInRange(now, timeSet.getSession2_2Start(), timeSet.getSession2_2End());
        };
    }

    private boolean isInRange(LocalTime now, LocalTime start, LocalTime end) {
        return (now.isAfter(start) || now.equals(start)) && (now.isBefore(end) || now.equals(end));
    }

    public ApiResponse<String> updateAttendanceStatus(UpdateDeviceRequest request) {
        LocalDate today = LocalDate.now();

        FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(request.studentId())
                .orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

        AttendanceTime attendanceTime = attendanceRepository.findByFingerPrint_IdAndToday(fingerPrint.getId(), today)
                .orElseThrow(() -> new ApplicationException(AttendanceStatus.NOT_IN_ATTENDANCE_TIME));

        switch (request.targetSession().toUpperCase()) {
            case "SESSION_1" -> attendanceTime.setS1Status(request.newStatus());
            case "SESSION_2" -> attendanceTime.setS2Status(request.newStatus());
            default -> throw new ApplicationException(AttendanceStatus.NOT_IN_ATTENDANCE_TIME);
        }

        attendanceRepository.save(attendanceTime);

        messagingTemplate.convertAndSend("/topic/attendance/update", (Object) Map.of(
                "studentId", request.studentId(),
                "session", request.targetSession(),
                "status", request.newStatus(),
                "timestamp", LocalTime.now().toString()
        ));

        return ApiResponse.ok("출석 상태가 변경되었습니다.");
    }

}