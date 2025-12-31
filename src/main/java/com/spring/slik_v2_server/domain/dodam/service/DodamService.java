package com.spring.slik_v2_server.domain.dodam.service;

import com.spring.slik_v2_server.domain.dodam.dto.request.CreateDBRequest;
import com.spring.slik_v2_server.domain.dodam.dto.response.CreateDBResponse;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.exception.DodamStatus;
import com.spring.slik_v2_server.domain.dodam.repository.DodamRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DodamService {

	private final DodamRepository dodamRepository;

	public ApiResponse<CreateDBResponse> createDB(CreateDBRequest request) {
		long studentId = Long.parseLong(String.format("%d%d%02d", request.grade(), request.room(), request.number()));

		if (dodamRepository.existsByStudentId(studentId)) {
			throw new ApplicationException(DodamStatus.ALREADY_CHECKED_STUDENT);
		}

		Dodam dodam = Dodam.builder()
				.studentId(studentId)
				.build();

		dodamRepository.save(dodam);
		return ApiResponse.ok(CreateDBResponse.toEntity(dodam));
	}
}
