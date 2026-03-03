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
import com.spring.slik_v2_server.domain.logs.service.LogsService;
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
    private final LogsService logsService;

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
                logsService.saveLog("Attendance is not available at this time.", "출석 가능 시간이 아닙니다.", false, student);
                return;
            }

            AttendanceType currentSessionType = currentSessionTypeOpt.get();

            if (currentSessionType.name().contains("session2") && attendanceTime.getType() == Type.NIGHT_STUDY_1) {
                socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                        "status", "FAILED",
                        "message", "심자1 대상은 심자2에 참여할 수 없습니다.",
                        "timestamp", LocalTime.now().toString()
                ));
                logsService.saveLog("Level 1 participants cannot join Level 2.", "심자1 대상은 심자2에 참여할 수 없습니다.", false, student);
                return;
            }

            LocalTime s1OutTimeBeforeUpdate = attendanceTime.getS1OutTime();
            String logType = attendanceTime.updateAttendanceTime(now, timeSet);
            LocalTime s1OutTimeAfterUpdate = attendanceTime.getS1OutTime();

            switch (logType) {
                case "S1_IN" -> logsService.saveLog("S1 In", "S1 출석", true, student);
                case "S1_OUT" -> logsService.saveLog("S1 Out", "S1 복귀 출석", true, student);
                case "S2_IN" -> logsService.saveLog("S2 In", "S2 출석", true, student);
                case "S2_OUT" -> logsService.saveLog("S2 Out", "S2 복귀 출석", true, student);
                case "S1_IN_ALREADY" -> {
                    logsService.saveLog("S1 In - Already", "S1 출석은 이미 완료되었습니다.", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1 출석은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S1_OUT_NO_IN" -> {
                    logsService.saveLog("S1 Out - No In", "S1In이 null - 퇴실 불가", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1In이 null - 퇴실 불가",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S1_OUT_ALREADY" -> {
                    logsService.saveLog("S1 Out - Already", "S1 퇴실은 이미 완료되었습니다.", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1 퇴실은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_IN_NO_S1_OUT" -> {
                    logsService.saveLog("S2 In - No S1 Out", "S1을 끝내지 못했습니다.", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S1을 끝내지 못했습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_IN_ALREADY" -> {
                    logsService.saveLog("S2 In - Already", "S2 출석은 이미 완료되었습니다.", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S2 출석은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_OUT_NO_IN" -> {
                    logsService.saveLog("S2 Out - No In", "S2In이 null - 퇴실 불가", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S2In이 null - 퇴실 불가",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "S2_OUT_ALREADY" -> {
                    logsService.saveLog("S2 Out - Already", "S2 퇴실은 이미 완료되었습니다.", false, student);
                    socketIOServer.getRoomOperations(request.deviceId()).sendEvent("verify", Map.of(
                            "status", "FAILED",
                            "message", "S2 퇴실은 이미 완료되었습니다.",
                            "timestamp", LocalTime.now().toString()
                    ));
                    return;
                }
                case "NO_UPDATE" -> {
                    logsService.saveLog("No Update", "유효하지 않은 출석 요청입니다.", false, student);
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
            logsService.saveLog("Exception", e.getMessage(), false, student);
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
                logsService.saveLog("S1 Status Update", "S1 상태 변경: " + request.newStatus(), true, attendanceTime.getStudent());
            }
            case "SESSION_2" -> {
                attendanceTime.setS2Status(request.newStatus());
                logsService.saveLog("S2 Status Update", "S2 상태 변경: " + request.newStatus(), true, attendanceTime.getStudent());
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