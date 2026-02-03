package com.spring.slik_v2_server.domain.logs.controller;

import com.spring.slik_v2_server.domain.logs.dto.response.GetLogsResponse;
import com.spring.slik_v2_server.domain.logs.service.LogsService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LogsController {
    private final LogsService service;

    @GetMapping("/admin/students/{studentId}/logs")
    public ApiResponse<List<GetLogsResponse>> getLog(@PathVariable String studentId) {
        return service.getLog(studentId);
    }
}
