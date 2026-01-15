package com.spring.slik_v2_server.domain.device.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyDeviceRequest(
        @NotBlank(message = "지문 id를 보내주세요.")
        Long id,
        @NotBlank(message = "device 아이디를 보내주세요.")
        String device_id
) {
}
