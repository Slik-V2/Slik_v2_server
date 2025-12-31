package com.spring.slik_v2_server.domain.dodam.service;

import com.spring.slik_v2_server.domain.dodam.dto.response.SaveStudentsResponse;
import com.spring.slik_v2_server.domain.dodam.dto.response.external.NightStudyResponse;
import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import com.spring.slik_v2_server.domain.dodam.repository.DodamRepository;
import com.spring.slik_v2_server.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DodamService {

	private final DodamRepository dodamRepository;

	@Transactional
	public List<Dodam> saveStudents(NightStudyResponse response) {
		if (response.data() == null || response.data().isEmpty()) {
			return null;
		}

		List<Dodam> students = response.data().stream()
				.map(item -> {
					NightStudyResponse.StudentInfo info = item.student();

					long studentId = Long.parseLong(String.format("%d%d%02d", info.grade(), info.room(), info.number()));

					return Dodam.builder()
							.studentId(studentId)
							.startAt(item.startAt())
							.endAt(item.endAt())
							.build();
				}).collect(Collectors.toList());

		List<Long> studentsId = students.stream()
				.map(Dodam::getStudentId)
				.toList();

		List<Dodam> existedStudents = dodamRepository.findAllByStudentIdIn(studentsId);

		List<Dodam> newStudents = students.stream()
				.filter(s -> !existedStudents.contains(s.getStudentId()))
				.toList();

		if (newStudents.isEmpty()) {
			return List.of();
		}

		return dodamRepository.saveAll(newStudents);
	}
}
