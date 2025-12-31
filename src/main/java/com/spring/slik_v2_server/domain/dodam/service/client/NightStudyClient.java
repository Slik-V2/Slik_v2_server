package com.spring.slik_v2_server.domain.dodam.service.client;

import com.spring.slik_v2_server.domain.dodam.dto.response.external.NightStudyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class NightStudyClient {

	private final WebClient webClient;

	public NightStudyResponse getNightStudyStudents() {
		return webClient.get()
				.uri("")
				.retrieve()
				.bodyToMono(NightStudyResponse.class)
				.block();
	}
}
