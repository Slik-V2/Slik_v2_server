package com.spring.slik_v2_server.domain.teacher.service;

import  com.spring.slik_v2_server.domain.teacher.dto.request.ChangePasswordRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.ChangeRoleRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.IsActiveRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignUpRequest;
import com.spring.slik_v2_server.domain.teacher.dto.response.GetTeacherResponse;
import com.spring.slik_v2_server.domain.teacher.dto.response.LoginResponse;
import com.spring.slik_v2_server.domain.teacher.entity.Role;
import com.spring.slik_v2_server.domain.teacher.entity.Teacher;
import com.spring.slik_v2_server.domain.teacher.exception.TeacherStatusCode;
import com.spring.slik_v2_server.domain.teacher.repository.TeacherRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import com.spring.slik_v2_server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    @Value("${spring.jwt.access-token-expiration}")  private int ACCESS_TOKEN_VALIDITY;

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
                .build());
        return ApiResponse.ok("선생님 계정이 생성되었습니다.");
    }

    public ApiResponse<LoginResponse> login(SignInRequest request) {
        Teacher teacher = teacherRepository.findByUsernameAndIsActiveTrue(request.id())
                .orElseThrow(() -> ApplicationException.of(TeacherStatusCode.TEACHER_NOT_FOUND));

        if(!bCryptPasswordEncoder.matches(request.password(), teacher.getPassword())) {
            throw ApplicationException.of(TeacherStatusCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(teacher.getUsername(),teacher.getRole());
        return ApiResponse.ok(LoginResponse.of(accessToken, ACCESS_TOKEN_VALIDITY, teacher));
    }

    public ApiResponse<?> changePassword(ChangePasswordRequest request) {
        Teacher teacher = getTeacher();

        if (bCryptPasswordEncoder.matches(request.newPassword(), teacher.getPassword())) {
            throw ApplicationException.of(TeacherStatusCode.SAME_AS_OLD_PASSWORD);
        }
        if (!bCryptPasswordEncoder.matches(request.currentPassword(), teacher.getPassword())) {
            throw new ApplicationException(TeacherStatusCode.PASSWORD_NOT_MATCH);
        }

        teacher.setPassword(bCryptPasswordEncoder.encode(request.newPassword()));
        teacherRepository.save(teacher);
        return ApiResponse.ok("비밀번호 변경 성공");
    }

    public ApiResponse<Boolean> isActive(String id, IsActiveRequest request) {
        Teacher teacher = teacherRepository.findByUsername(id)
                .orElseThrow(() -> ApplicationException.of(TeacherStatusCode.TEACHER_NOT_FOUND));
        teacher.setActive(request.isActive());
        teacherRepository.save(teacher);
        return ApiResponse.ok(Boolean.TRUE);
    }

    public ApiResponse<List<GetTeacherResponse>> getTeachers() {
        List<GetTeacherResponse> responses = teacherRepository.findAll().stream().map(
                GetTeacherResponse::of
        ).collect(Collectors.toList());
        return ApiResponse.ok(responses);
    }

    public ApiResponse<?> isRole(ChangeRoleRequest request, String userId) {
        Teacher teacher = teacherRepository.findByUsername(userId)
                .orElseThrow(() -> new ApplicationException(TeacherStatusCode.TEACHER_NOT_FOUND));

        switch (request.role()) {
            case "teacher" -> teacher.setRole(Role.TEACHER);
            case "admin" -> teacher.setRole(Role.ADMIN);
        }
        teacherRepository.save(teacher);

        return ApiResponse.ok(userId+"님의 role값이 변경되었습니다.");
    }
}
