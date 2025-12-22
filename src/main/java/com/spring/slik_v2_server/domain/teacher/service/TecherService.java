package com.spring.slik_v2_server.domain.teacher.service;

import com.spring.slik_v2_server.domain.teacher.dto.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.TeacherRequest;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;
import com.spring.slik_v2_server.domain.teacher.exception.TeacherErrorCode;
import com.spring.slik_v2_server.domain.teacher.exception.TeacherException;
import com.spring.slik_v2_server.domain.teacher.repository.TeacherRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TecherService {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    public ApiResponse<String> createTeacher(TeacherRequest request) {
        teacherRepository.save(Teacher.builder()
                .username(request.id())
                .password(bCryptPasswordEncoder.encode(request.password()))
                .email(request.email())
                .phone(request.phone())
                .build());
        return ApiResponse.ok("선생님 계정이 생성되었습니다.");
    }

    public ApiResponse<String> signin(SignInRequest request) {
        Teacher teacher = teacherRepository.findByUsername(request.id())
                .orElseThrow(() -> new TeacherException(TeacherErrorCode.TEACHER_NOT_FOUND));

        if(!bCryptPasswordEncoder.matches(request.password(), teacher.getPassword())) {
            throw new TeacherException(TeacherErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(teacher.getUsername(),teacher.getRole());

        return ApiResponse.ok(accessToken);
    }
}
