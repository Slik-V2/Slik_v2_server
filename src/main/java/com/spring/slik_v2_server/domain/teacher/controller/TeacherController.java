package com.spring.slik_v2_server.domain.teacher.controller;

import com.spring.slik_v2_server.domain.teacher.dto.SignInRequest;
import com.spring.slik_v2_server.domain.teacher.dto.TeacherRequest;
import com.spring.slik_v2_server.domain.teacher.service.TecherService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TecherService techerService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody TeacherRequest request) {
        ApiResponse<String> response = techerService.createTeacher(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> signin(@RequestBody SignInRequest request) {
        ApiResponse<String> response = techerService.signin(request);
        return ResponseEntity.ok(response);
    }
}