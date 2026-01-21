package com.spring.slik_v2_server.domain.dodam.service;

import com.spring.slik_v2_server.domain.dodam.dto.response.external.NightStudyResponse;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.repository.DodamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DodamService {

	private final DodamRepository dodamRepository;
	private final WebClient webClient;

	public List<Dodam>  saveStudent() {
		NightStudyResponse response;
		try {
			response = webClient.get()
					.retrieve()
					.bodyToMono(NightStudyResponse.class)
					.block();
		} catch (Exception e) {
			log.error("심자신청자 명단을 불러올 수 없습니다. 에러는 다음과 같습니다.\n", e);
			return List.of();
		}

		return saveStudents(response);
	}

	@Transactional
	public List<Dodam> saveStudents(NightStudyResponse response) {
		dodamRepository.deleteByEndAtBefore(LocalDate.now());

		List<Dodam> students = response.data().stream()
				.map(item -> {
					NightStudyResponse.StudentInfo info = item.student();

					long studentId = Long.parseLong(String.format("%d%d%02d", info.grade(), info.room(), info.number()));

					return Dodam.builder()
							.studentId(studentId)
							.type(item.type())
							.startAt(item.startAt())
							.endAt(item.endAt())
							.build();
				}).collect(Collectors.toList());

		List<Long> studentsId = students.stream()
				.map(Dodam::getStudentId)
				.toList();

		List<Dodam> existedStudents = dodamRepository.findAllByStudentIdIn(studentsId);

		Set<Long> existedStudentsIds = existedStudents.stream()
				.map(Dodam::getStudentId)
				.collect(Collectors.toSet());

		List<Dodam> newStudents = students.stream()
				.filter(s -> !existedStudentsIds.contains(s.getStudentId()))
				.toList();

		if (!newStudents.isEmpty()) {
			dodamRepository.saveAll(newStudents);
		}

		return dodamRepository.findAllByStudentIdIn(studentsId);
	}
}
