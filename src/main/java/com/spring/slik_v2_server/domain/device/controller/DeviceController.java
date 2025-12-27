package com.spring.slik_v2_server.domain.device.controller;

import com.spring.slik_v2_server.domain.device.dto.request.DeviceRequest;
import com.spring.slik_v2_server.domain.device.service.DeviceService;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/verify")
    public ApiResponse<String> verify(@RequestHeader("X-API-KEY") String apikey, @RequestBody DeviceRequest request) {
        return deviceService.Verification(request, apikey);
    }
}
