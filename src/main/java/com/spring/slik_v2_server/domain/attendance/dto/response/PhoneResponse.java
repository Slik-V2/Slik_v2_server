package com.spring.slik_v2_server.domain.attendance.dto.response;

import com.spring.slik_v2_server.domain.dodam.entity.Dodam;

public record PhoneResponse(
        boolean status,
        String reason
) {
    public static PhoneResponse of(Dodam dodam) {
        return new PhoneResponse(
                dodam.isDoNeedPhone(),
                dodam.isDoNeedPhone() ? dodam.getReasonForPhone() : null
        );
    }
}
