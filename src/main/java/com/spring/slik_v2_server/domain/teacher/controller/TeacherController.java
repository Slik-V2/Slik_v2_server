package com.spring.slik_v2_server.domain.teacher.controller;

import com.spring.slik_v2_server.domain.teacher.dto.request.ChagePsswordRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.request.SignUpRequest;
import com.spring.slik_v2_server.domain.teacher.service.TeacherService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import jakarta.validation.Valid;
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
    public ApiResponse<String> login(@Valid @RequestBody SignInRequest request) {
        return teacherService.login(request);
    }

    @PutMapping("/password")
    public ApiResponse<?> chagePassword(@RequestBody ChagePsswordRequest request) {
        return teacherService.chagePassword(request);
    }
}