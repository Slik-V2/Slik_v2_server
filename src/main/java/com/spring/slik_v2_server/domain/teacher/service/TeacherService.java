package com.spring.slik_v2_server.domain.teacher.service;

import com.spring.slik_v2_server.domain.teacher.dto.request.ChagePsswordRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignUpRequest;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;
import com.spring.slik_v2_server.domain.teacher.exception.TeacherErrorCode;
import com.spring.slik_v2_server.domain.teacher.exception.TeacherException;
import com.spring.slik_v2_server.domain.teacher.repository.TeacherRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    public Teacher getTeacher() {
        Authentication auth = SecurityContextHolder.createEmptyContext().getAuthentication();
        return teacherRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new TeacherException(TeacherErrorCode.TEACHER_NOT_FOUND));
    }

    public ApiResponse<String> createTeacher(SignUpRequest request) {
        teacherRepository.save(Teacher.builder()
                .username(request.id())
                .password(bCryptPasswordEncoder.encode(request.password()))
                .email(request.email())
                .phone(request.phone())
                .build());
        return ApiResponse.ok("선생님 계정이 생성되었습니다.");
    }

    public ApiResponse<String> login(SignInRequest request) {
        Teacher teacher = teacherRepository.findByUsername(request.id())
                .orElseThrow(() -> new TeacherException(TeacherErrorCode.TEACHER_NOT_FOUND));

        if(!bCryptPasswordEncoder.matches(request.password(), teacher.getPassword())) {
            throw new TeacherException(TeacherErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(teacher.getUsername(),teacher.getRole());
        return ApiResponse.ok(accessToken);
    }

    public ApiResponse<?> chagePassword(ChagePsswordRequest request) {
        Teacher teacher = getTeacher();

        if (bCryptPasswordEncoder.matches(request.new_password(), teacher.getPassword())) {
            throw new TeacherException(TeacherErrorCode.SAME_AS_OLD_PASSWORD);
        }

        teacher.setPassword(bCryptPasswordEncoder.encode(request.new_password()));
        teacherRepository.save(teacher);
        return ApiResponse.ok("비밀번호 변경 성공");
    }
}
