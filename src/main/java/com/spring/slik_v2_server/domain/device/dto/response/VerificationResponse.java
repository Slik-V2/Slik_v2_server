package com.spring.slik_v2_server.domain.device.dto.response;

public record VerificationResponse(
        boolean success,
        String message
) {
}
