package com.spring.slik_v2_server.domain.dodam.scheduler;

import com.spring.slik_v2_server.domain.dodam.dto.response.external.NightStudyResponse;
import com.spring.slik_v2_server.domain.dodam.service.DodamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class DodamSceduler {

	private final DodamService dodamService;
	private final WebClient webClient;

	@Scheduled(cron = "0 35 20 * * *")
	public void saveStudents() {
		log.info("심자신청자 명단을 저장하고 있습니다. 잠시만 기다려 주세요. . .");

		try {
			NightStudyResponse response = webClient.get()
					.retrieve()
					.bodyToMono(NightStudyResponse.class)
					.block();

			if (response != null && response.data() != null) {
				dodamService.saveStudents(response);
				log.info("심사신청자 명단을 저장했습니다.");
			} else {
				log.warn("심자신청자 명단을 불러올 수 없습니다.");
			}
		} catch (Exception e) {
			log.error("심자신청자 명단을 불러오는데 알 수 없는 문제가 발생했습니다. 에러는 아래와 같습니다.\n", e);
		}
	}

}
