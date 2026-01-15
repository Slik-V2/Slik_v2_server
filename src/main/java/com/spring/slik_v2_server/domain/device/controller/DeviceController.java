package com.spring.slik_v2_server.domain.device.controller;

import com.spring.slik_v2_server.domain.device.dto.request.FindAttendanceRequest;
import com.spring.slik_v2_server.domain.device.dto.request.UpdateDeviceRequest;
import com.spring.slik_v2_server.domain.device.service.DeviceService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device/override   ")
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/lookup")
    public ApiResponse<?> lookup(@Valid @RequestBody FindAttendanceRequest request) {
        return deviceService.findAttendanceStatus(request.studnet_id());

    }

    @PatchMapping("/confirm")
    public ApiResponse<?> updateDevice(@Valid @RequestBody UpdateDeviceRequest request) {
        return deviceService.updateAttendanceStatus(request);
    }
}
