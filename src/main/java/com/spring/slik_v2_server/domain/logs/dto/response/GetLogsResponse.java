package com.spring.slik_v2_server.domain.logs.dto.response;

import com.spring.slik_v2_server.domain.logs.entity.Logs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GetLogsResponse(
        LocalDateTime timestamp,
        String action,
        String message,
        boolean success
) {
    public static GetLogsResponse of(Logs logs) {
        return new GetLogsResponse(
                logs.getTimestamp(),
                logs.getAction(),
                logs.getMessage(),
                logs.isSuccess()
        );
    }

    public static List<GetLogsResponse> fromList(List<Logs> logsList) {
        return logsList.stream().map(GetLogsResponse::of).collect(Collectors.toList());
    }
}
