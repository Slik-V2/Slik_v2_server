package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.LiveAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.StudentAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTimeSet;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.exception.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceSetRepository;
import com.spring.slik_v2_server.domain.device.dto.request.UpdateDeviceRequest;
import com.spring.slik_v2_server.domain.device.dto.request.VerifyDeviceRequest;
import com.spring.slik_v2_server.domain.device.dto.response.DeviceResponse;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.repository.DodamRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceSetRepository attendanceSetRepository;
    private final FingerPrintRepository fingerPrintRepository;
    private final StudentRepository studentRepository;
    private final DodamRepository dodamRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ApiResponse<DeviceResponse> verifyAttendance(VerifyDeviceRequest request) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Student student = studentRepository.findByFingerPrint_Id(request.id())
                .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

        AttendanceTimeSet timeSet = attendanceSetRepository.findByToday(today)
                .orElseThrow(() -> new ApplicationException(AttendanceStatus.NOT_IN_ATTENDANCE_TIME));

        AttendanceTime attendanceTime = attendanceRepository.findByFingerPrint_IdAndToday(request.id(), today)
                .orElseThrow(() -> new ApplicationException(AttendanceStatus.NOT_IN_ATTENDANCE_TIME));

        attendanceTime.updateAttendanceTime(now, timeSet);
        attendanceRepository.save(attendanceTime);

        messagingTemplate.convertAndSend("/topic/device/" + request.device_id());

        return ApiResponse.ok(DeviceResponse.of(student.getName()));
    }

    public ApiResponse<List<AttendanceTimeResponse>> findAttendanceStatus(String id) {
        LocalDate today = LocalDate.now();

        FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(id)
                .orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

        List<AttendanceTime> attendanceTimes = attendanceRepository.findAllByFingerPrintAndTodayBetween(fingerPrint, today, today);
        List<AttendanceTimeResponse> attendanceStatus = AttendanceTimeResponse.fromList(attendanceTimes);
        return ApiResponse.ok(attendanceStatus);
    }

    public ApiResponse<LiveAttendanceResponse> getLiveAttendance(LocalDate date) {
        List<StudentAttendanceResponse> attendanceResponses = attendanceRepository.findAllByToday(date).stream()
                .map(StudentAttendanceResponse::of)
                .collect(Collectors.toList());

        return ApiResponse.ok(LiveAttendanceResponse.of(date, attendanceResponses));
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

    public void createDailyAttendanceRecords() {
        LocalDate today = LocalDate.now();

        // 1. 활동 중인 모든 Dodam 조회
        List<Dodam> activeDodams = dodamRepository.findAllByStartAtLessThanEqualAndEndAtGreaterThanEqual(today, today);

        if (activeDodams.isEmpty()) {
            return;
        }

        // 2. 학생 ID 추출
        List<Long> studentIds = activeDodams.stream()
                .map(Dodam::getStudentId)
                .distinct()
                .toList();

        // 3. 필요한 FingerPrint들 한 번에 조회
        Map<Long, FingerPrint> fingerPrintMap = fingerPrintRepository
                .findAllById(studentIds)
                .stream()
                .collect(Collectors.toMap(fp -> Long.parseLong(fp.getId()), Function.identity()));

        // 4. 생성할 AttendanceTime 배치로 준비
        List<AttendanceTime> newAttendances = activeDodams.stream()
                .map(dodam -> {
                    FingerPrint fingerPrint = fingerPrintMap.get(dodam.getStudentId());
                    if (fingerPrint == null) {
                        return null;
                    }
                    return AttendanceTime.builder()
                            .today(today)
                            .fingerPrint(fingerPrint)
                            .s1Status(com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus.STUDYING)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();

        // 5. 배치 저장
        if (!newAttendances.isEmpty()) {
            attendanceRepository.saveAll(newAttendances);
        }
    }
}
