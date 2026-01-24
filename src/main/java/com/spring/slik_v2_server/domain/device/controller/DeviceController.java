package com.spring.slik_v2_server.domain.device.controller;

import com.spring.slik_v2_server.domain.device.dto.response.OverrideLookupResponse;
import com.spring.slik_v2_server.domain.device.service.AttendanceQueryService;
import com.spring.slik_v2_server.domain.device.dto.request.UpdateDeviceRequest;
import com.spring.slik_v2_server.domain.device.dto.request.VerifyDeviceRequest;
import com.spring.slik_v2_server.domain.device.service.DeviceService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;
    private final AttendanceQueryService attendanceQueryService;

    @PostMapping("/verify")
    public ApiResponse<String> verify(@Valid @RequestBody VerifyDeviceRequest request) {
        deviceService.verifyAttendance(request);
        return ApiResponse.ok("출석 인증 요청이 처리되었습니다.");
    }

    @GetMapping("/override/lookup")
    public ApiResponse<OverrideLookupResponse> lookup(@RequestParam String studentId) {
        return attendanceQueryService.findOverrideLookup(studentId);
    }

    @PatchMapping("/override/confirm")
    public ApiResponse<String> updateDevice(@Valid @RequestBody UpdateDeviceRequest request) {
        return deviceService.updateAttendanceStatus(request);
    }
}
