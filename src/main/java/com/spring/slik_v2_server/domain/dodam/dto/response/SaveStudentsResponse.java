package com.spring.slik_v2_server.domain.dodam.dto.response;

import com.spring.slik_v2_server.domain.dodam.entity.Dodam;

import java.util.List;

public record SaveStudentsResponse(
		List<Long> studentId
) {

	public static SaveStudentsResponse of(List<Dodam> dodam) {
		List<Long> studentIds = dodam.stream()
				.map(Dodam::getStudentId)
				.toList();
		return new SaveStudentsResponse(studentIds);
	}
}
