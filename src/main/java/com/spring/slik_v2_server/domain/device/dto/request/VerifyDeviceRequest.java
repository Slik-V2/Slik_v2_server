package com.spring.slik_v2_server.domain.device.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyDeviceRequest(
        String id,
        @NotBlank(message = "device 아이디를 보내주세요.")
        String device_id
) {
}
