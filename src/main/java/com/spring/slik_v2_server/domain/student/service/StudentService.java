package com.spring.slik_v2_server.domain.student.service;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.fingerprint.exception.FingerPrintStatusCode;
import com.spring.slik_v2_server.domain.fingerprint.repository.FingerPrintRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.domain.student.exception.StudentStatus;
import com.spring.slik_v2_server.domain.student.repository.StudentRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final FingerPrintRepository fingerPrintRepository;

    public ApiResponse<HttpStatus> saveStudentInfo(String studentId) {
        Student student = studentRepository.findByStudentId(String.valueOf(studentId))
                .orElseThrow(() -> new ApplicationException(StudentStatus.STUDENT_NOT_FOUND));

        FingerPrint fingerPrint = fingerPrintRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ApplicationException(FingerPrintStatusCode.STUDENT_NOT_FOUND));

        student.updateFingerPrint(fingerPrint);
        studentRepository.save(student);

        return ApiResponse.ok(HttpStatus.CREATED);
    }
}
