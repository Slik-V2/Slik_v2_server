package com.spring.slik_v2_server.domain.dodam.scheduler;

import com.spring.slik_v2_server.domain.dodam.service.DodamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DodamScheduler {

	private final DodamService dodamService;

	@Scheduled(cron = "0 50 20 * * mon-thu")
	public void saveStudents() {
		log.info("심자신청자 명단을 저장하고 있습니다. 잠시만 기다려 주세요. . .");

		try {
			dodamService.saveStudent();
			log.info("심사신청자 명단을 저장했습니다.");
		} catch (Exception e) {
			log.error("심자신청자 명단을 불러오는데 알 수 없는 문제가 발생했습니다. 에러는 아래와 같습니다.\n", e);
		}
	}
}
