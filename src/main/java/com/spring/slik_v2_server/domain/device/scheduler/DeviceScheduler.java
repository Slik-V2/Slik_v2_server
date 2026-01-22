package com.spring.slik_v2_server.domain.device.scheduler;

import com.spring.slik_v2_server.domain.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceScheduler {
    private final DeviceService deviceService;

    @Scheduled(cron = "0 51 20 * * mon-thu")
    public void saveAttendanceStatus() {
        log.info("일일 출석 기록 생성 스케줄러 시작");

        try {
            deviceService.createDailyAttendanceRecords();
            log.info("일일 출석 기록이 정상적으로 생성되었습니다.");
        } catch (Exception e) {
            log.error("일일 출석 기록 생성 중 오류가 발생했습니다.", e);
        }
    }
}