package com.spring.slik_v2_server.domain.device.dto.response;

public record DeviceResponse(
        String name
) {
    public static DeviceResponse of(String name) {
        return new DeviceResponse(name + "님이 출석하셨습니다.");
    }
}
