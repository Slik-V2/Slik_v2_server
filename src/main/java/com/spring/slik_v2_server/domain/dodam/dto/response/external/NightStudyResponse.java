package com.spring.slik_v2_server.domain.dodam.dto.response.external;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.slik_v2_server.domain.dodam.entity.Type;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NightStudyResponse (
		List<NightStudyItem> data
) {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record NightStudyItem(
			StudentInfo student,

			@Enumerated(EnumType.STRING)
			Type type,

			LocalDate startAt,
			LocalDate endAt,

			boolean doNeedPhone,
			String reasonForPhone
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
