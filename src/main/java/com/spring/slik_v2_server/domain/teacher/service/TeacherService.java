package com.spring.slik_v2_server.domain.teacher.service;

import  com.spring.slik_v2_server.domain.teacher.dto.request.ChangePasswordRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignUpRequest;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;
import com.spring.slik_v2_server.domain.teacher.exception.TeacherStatusCode;
import com.spring.slik_v2_server.domain.teacher.repository.TeacherRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return teacherRepository.findByUsername(auth.getName())
                .orElseThrow(() -> ApplicationException.of(TeacherStatusCode.TEACHER_NOT_FOUND));
    }

    public ApiResponse<String> createTeacher(SignUpRequest request) {
        teacherRepository.save(Teacher.builder()
                .username(request.id())
                .password(bCryptPasswordEncoder.encode(request.password()))
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .build());
        return ApiResponse.ok("선생님 계정이 생성되었습니다.");
    }

    public ApiResponse<String> login(SignInRequest request) {
        Teacher teacher = teacherRepository.findByUsernameAndIsActiveTrue(request.id())
                .orElseThrow(() -> ApplicationException.of(TeacherStatusCode.TEACHER_NOT_FOUND));

        if(!bCryptPasswordEncoder.matches(request.password(), teacher.getPassword())) {
            throw ApplicationException.of(TeacherStatusCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(teacher.getUsername(),teacher.getRole());
        return ApiResponse.ok(accessToken);
    }

    public ApiResponse<?> changePassword(ChangePasswordRequest request) {
        Teacher teacher = getTeacher();

        if (bCryptPasswordEncoder.matches(request.new_password(), teacher.getPassword())) {
            throw ApplicationException.of(TeacherStatusCode.SAME_AS_OLD_PASSWORD);
        } else if (bCryptPasswordEncoder.matches(teacher.getPassword(), request.current_password())) {
            throw new ApplicationException(TeacherStatusCode.PASSWORD_NOT_MATCH);
        }

        teacher.setPassword(bCryptPasswordEncoder.encode(request.new_password()));
        teacherRepository.save(teacher);
        return ApiResponse.ok("비밀번호 변경 성공");
    }

    public ApiResponse<Boolean> isActive(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> ApplicationException.of(TeacherStatusCode.TEACHER_NOT_FOUND));
        teacher.setActive();
        teacherRepository.save(teacher);
        return ApiResponse.ok(Boolean.TRUE);
    }
}
