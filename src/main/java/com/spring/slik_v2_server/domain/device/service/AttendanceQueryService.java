package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.attendance.dto.response.AttendanceTimeResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.LiveAttendanceResponse;
import com.spring.slik_v2_server.domain.attendance.dto.response.StudentAttendanceResponse;
import com.spring.slik_v2_server.domain.device.dto.response.OverrideLookupResponse;
import com.spring.slik_v2_server.domain.student.exception.StudentStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.repository.DodamRepository;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.domain.student.repository.StudentRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceQueryService {
    private final AttendanceRepository attendanceRepository;
    private final FingerPrintRepository fingerPrintRepository;
    private final StudentRepository studentRepository;
    private final DodamRepository dodamRepository;

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

    public ApiResponse<OverrideLookupResponse> findOverrideLookup(String studentId) {
        LocalDate today = LocalDate.now();

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

        AttendanceTime attendanceTime = attendanceRepository.findByStudentAndToday(student, today)
                .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

        return ApiResponse.ok(OverrideLookupResponse.of(attendanceTime));
    }

    public void createDailyAttendanceRecords() {
        LocalDate today = LocalDate.now();
        List<Dodam> activeDodams = dodamRepository.findAllByStartAtLessThanEqualAndEndAtGreaterThanEqual(today, today);

        List<AttendanceTime> newAttendances = activeDodams.stream()
                .map(dodam -> {
                    FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(String.valueOf(dodam.getStudentId()))
                            .orElse(null);
                    Student student = studentRepository.findByStudentId(String.valueOf(dodam.getStudentId()))
                            .orElse(null);

                    if (student != null) {
                        student.plusDate();
                        studentRepository.save(student);
                    }

                    return AttendanceTime.builder()
                            .today(today)
                            .fingerPrint(fingerPrint)
                            .type(dodam.getType())
                            .s1Status(AttendanceStatus.NONE)
                            .s2Status(AttendanceStatus.NONE)
                            .student(student)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();

        if (!newAttendances.isEmpty()) {
            attendanceRepository.saveAll(newAttendances);
        }
    }
}
