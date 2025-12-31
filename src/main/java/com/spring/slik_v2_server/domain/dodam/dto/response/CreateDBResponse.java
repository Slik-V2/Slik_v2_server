package com.spring.slik_v2_server.domain.dodam.dto.response;

import com.spring.slik_v2_server.domain.dodam.dto.request.CreateDBRequest;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;

public record CreateDBResponse(
		long studentId
) {

	public static CreateDBResponse toEntity(Dodam dodam) {
		return new CreateDBResponse(dodam.getStudentId());
	}
}
