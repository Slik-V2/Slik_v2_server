package com.spring.slik_v2_server.domain.device.dto.request;

public record DeviceRequest(
        String device_id,
        String encrypted_template
) {
}
