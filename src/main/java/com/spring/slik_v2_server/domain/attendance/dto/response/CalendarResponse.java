package com.spring.slik_v2_server.domain.attendance.dto.response;

import java.util.Map;

public record CalendarResponse(Map<Integer, String> calendar) {}