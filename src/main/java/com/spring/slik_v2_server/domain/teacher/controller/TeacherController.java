package com.spring.slik_v2_server.domain.teacher.controller;

import com.spring.slik_v2_server.domain.teacher.dto.request.ChangePasswordRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.IsActiveRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignUpRequest;
import com.spring.slik_v2_server.domain.teacher.dto.response.LoginResponse;
import com.spring.slik_v2_server.domain.teacher.service.TeacherService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/signup")
    public ApiResponse<String> signup(@Valid @RequestBody SignUpRequest request) {
        return teacherService.createTeacher(request);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody SignInRequest request) {
        return teacherService.login(request);
    }

    @PutMapping("/password")
    public ApiResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return teacherService.changePassword(request);
    }

    @GetMapping("/users")
    public ApiResponse<?> getTeachers() {
        return teacherService.getTeachers();
    }

    @PatchMapping("/active/{id}")
    public ApiResponse<Boolean> isActive(@PathVariable String id, @RequestBody IsActiveRequest request) {
        return teacherService.isActive(id, request);
    }
}