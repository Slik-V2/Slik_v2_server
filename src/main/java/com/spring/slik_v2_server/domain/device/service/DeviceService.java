package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.exception.AttendanceTimeStatus;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceSetRepository;
import com.spring.slik_v2_server.domain.device.dto.request.UpdateDeviceRequest;
import com.spring.slik_v2_server.domain.device.dto.request.VerifyDeviceRequest;
import com.spring.slik_v2_server.domain.dodam.entity.Type;
import com.spring.slik_v2_server.domain.logs.entity.Logs;
import com.spring.slik_v2_server.domain.logs.repository.LogsRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.domain.student.exception.StudentStatus;
import com.spring.slik_v2_server.domain.student.repository.StudentRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceSetRepository attendanceSetRepository;
    private final StudentRepository studentRepository;
    private final SocketIOServer socketIOServer;
    private final LogsRepository logsRepository;

    public void verifyAttendance(VerifyDeviceRequest request) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        try {
            Student student = studentRepository.findByStudentId(request.id())
                    .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

            AttendanceTimeSet timeSet = attendanceSetRepository.findByToday(today)
                    .orElseThrow(() -> new ApplicationException(AttendanceTimeStatus.NOT_IN_ATTENDANCE_TIME));

            AttendanceTime attendanceTime = attendanceRepository.findByStudentAndToday(student, today)
                    .orElseThrow(() -> new ApplicationException(AttendanceTimeStatus.NOT_IN_ATTENDANCE_TIME));

            java.util.Optional<AttendanceType> currentSessionTypeOpt = getTypeByTimeRange(now, timeSet);

            if (currentSessionTypeOpt.isEmpty()) {
                socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                        "status", "FAILED",
                        "message", "출석 가능 시간이 아닙니다.",
                        "timestamp", LocalTime.now().toString()
                ));
                logsRepository.save(Logs.builder()
                        .action("Attendance is not available at this time.")
                        .message("출석 가능 시간이 아닙니다.")
                        .success(false)
                        .student(student)
                        .build());
                return;
            }

            AttendanceType currentSessionType = currentSessionTypeOpt.get();

            if (currentSessionType.name().contains("session2") && attendanceTime.getType() == Type.NIGHT_STUDY_1) {
                socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                        "status", "FAILED",
                        "message", "심자1 대상은 심자2에 참여할 수 없습니다.",
                        "timestamp", LocalTime.now().toString()
                ));
                logsRepository.save(Logs.builder()
                        .action("Level 1 participants cannot join Level 2.")
                        .message("심자1 대상은 심자2에 참여할 수 없습니다.")
                        .success(false)
                        .student(student)
                        .build());
                return;
            }

            LocalTime s1OutTimeBeforeUpdate = attendanceTime.getS1OutTime();
            String logType = attendanceTime.updateAttendanceTime(now, timeSet);
            LocalTime s1OutTimeAfterUpdate = attendanceTime.getS1OutTime();

            switch (logType) {
                case "S1_IN" -> {
                    logsRepository.save(Logs.builder()
                            .action("S1 In")
                            .message("S1 출석")
                            .success(true)
                            .student(student)
                            .build());
                }
                case "S1_OUT" -> {
                    logsRepository.save(Logs.builder()
                            .action("S1 Out")
                            .message("S1 복귀 출석")
                            .success(true)
                            .student(student)
                            .build());
                }
                case "S2_IN" -> {
                    logsRepository.save(Logs.builder()
                            .action("S2 In")
                            .message("S2 출석")
                            .success(true)
                            .student(student)
                            .build());
                }
                case "S2_OUT" -> {
                    logsRepository.save(Logs.builder()
                            .action("S2 Out")
                            .message("S2 복귀 출석")
                            .success(true)
                            .student(student)
                            .build());
                }
                case "S1_IN_ALREADY" -> {
                    logsRepository.save(Logs.builder()
                            .action("S1 In - Already")
                            .message("S1 출석은 이미 완료되었습니다.")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1 출석은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S1_OUT_NO_IN" -> {
                    logsRepository.save(Logs.builder()
                            .action("S1 Out - No In")
                            .message("S1In이 null - 퇴실 불가")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1In이 null - 퇴실 불가",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S1_OUT_ALREADY" -> {
                    logsRepository.save(Logs.builder()
                            .action("S1 Out - Already")
                            .message("S1 퇴실은 이미 완료되었습니다.")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1 퇴실은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_IN_NO_S1_OUT" -> {
                    logsRepository.save(Logs.builder()
                            .action("S2 In - No S1 Out")
                            .message("S1을 끝내지 못했습니다.")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1을 끝내지 못했습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_IN_ALREADY" -> {
                    logsRepository.save(Logs.builder()
                            .action("S2 In - Already")
                            .message("S2 출석은 이미 완료되었습니다.")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S2 출석은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_OUT_NO_IN" -> {
                    logsRepository.save(Logs.builder()
                            .action("S2 Out - No In")
                            .message("S2In이 null - 퇴실 불가")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S2In이 null - 퇴실 불가",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_OUT_ALREADY" -> {
                    logsRepository.save(Logs.builder()
                            .action("S2 Out - Already")
                            .message("S2 퇴실은 이미 완료되었습니다.")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S2 퇴실은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "NO_UPDATE" -> {
                    logsRepository.save(Logs.builder()
                            .action("No Update")
                            .message("유효하지 않은 출석 요청입니다.")
                            .success(false)
                            .student(student)
                            .build());
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "유효하지 않은 출석 요청입니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
            }

            if (s1OutTimeBeforeUpdate == null && s1OutTimeAfterUpdate != null &&
                attendanceTime.getType() == Type.NIGHT_STUDY_1) {
                student.increaseAttendanceDate();
            }

            attendanceRepository.save(attendanceTime);
            studentRepository.save(student);

            socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                    "status", "SUCCESS",
                    "name", student.getName(),
                    "timestamp", LocalTime.now().toString()
            ));
        } catch (ApplicationException e) {
            Student student = studentRepository.findByStudentId(request.id()).orElse(null);
            logsRepository.save(Logs.builder()
                    .action("Exception")
                    .message(e.getMessage())
                    .success(false)
                    .student(student)
                    .build());
            socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
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

        Student student = studentRepository.findByStudentId(request.studentId())
                .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

        AttendanceTime attendanceTime = attendanceRepository.findByStudentAndToday(student, today)
                .orElseThrow(() -> new ApplicationException(AttendanceTimeStatus.NOT_IN_ATTENDANCE_TIME));

        String session = request.targetSession().toUpperCase();

        switch (session) {
            case "SESSION_1" -> {
                attendanceTime.setS1Status(request.newStatus());
                logsRepository.save(Logs.builder()
                        .action("S1 Status Update")
                        .message("S1 상태 변경: " + request.newStatus())
                        .success(true)
                        .student(attendanceTime.getStudent())
                        .build());
            }
            case "SESSION_2" -> {
                attendanceTime.setS2Status(request.newStatus());
                logsRepository.save(Logs.builder()
                        .action("S2 Status Update")
                        .message("S2 상태 변경: " + request.newStatus())
                        .success(true)
                        .student(attendanceTime.getStudent())
                        .build());
            }
            default -> throw new ApplicationException(AttendanceTimeStatus.NOT_IN_ATTENDANCE_TIME);
        }

        attendanceRepository.save(attendanceTime);

        socketIOServer.getBroadcastOperations().sendEvent("attendanceUpdate", Map.of(
                "studentId", request.studentId(),
                "session", request.targetSession(),
                "status", request.newStatus(),
                "timestamp", LocalTime.now().toString()
        ));

        return ApiResponse.ok("출석 상태가 변경되었습니다.");
    }

}