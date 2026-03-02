package com.spring.slik_v2_server.domain.logs.service;

import com.spring.slik_v2_server.domain.logs.dto.response.GetLogsResponse;
import com.spring.slik_v2_server.domain.logs.entity.Logs;
import com.spring.slik_v2_server.domain.logs.repository.LogsRepository;
import com.spring.slik_v2_server.domain.student.entity.Student;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogsService {
    private final LogsRepository logsRepository;

    public ApiResponse<List<GetLogsResponse>> getLog(String studentId) {
        List<Logs> logs = logsRepository.findAllByStudent_StudentId(studentId);

        List<GetLogsResponse> responses = GetLogsResponse.fromList(logs);

        return ApiResponse.ok(responses);
    }

    @Async("logTaskExecutor")
    public void saveLog(String action, String message, boolean success, Student student) {
        logsRepository.save(Logs.builder()
                .action(action)
                .message(message)
                .success(success)
                .student(student)
                .build());
    }
}
