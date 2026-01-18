package com.spring.slik_v2_server.domain.attendance.scheduler;

import com.spring.slik_v2_server.domain.attendance.dto.request.AttendanceTimeSetRequest;
import com.spring.slik_v2_server.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendanceSetScheduler {

	private final AttendanceService attendanceService;

	@Scheduled(cron = "0 0 20 * * mon-thu")
	public void setDefaultScheduler() {
		AttendanceTimeSetRequest defaultSchedule = new AttendanceTimeSetRequest(
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		);
		attendanceService.setSchedule(defaultSchedule);
	}
}