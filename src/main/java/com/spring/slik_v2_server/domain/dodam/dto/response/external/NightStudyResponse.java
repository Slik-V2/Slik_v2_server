package com.spring.slik_v2_server.domain.dodam.dto.response.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

public record NightStudyResponse (
		List<NightStudyItem> data
) {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record NightStudyItem(
			StudentInfo student,
			LocalDate startAt,
			LocalDate endAt
	) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record StudentInfo(
			int grade,
			int room,
			int number
	) {
	}
}
