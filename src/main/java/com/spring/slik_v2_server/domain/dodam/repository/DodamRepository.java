package com.spring.slik_v2_server.domain.dodam.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.slik_v2_server.domain.dodam.entity.Dodam;

public interface DodamRepository extends JpaRepository<Dodam, Long> {

	List<Dodam> findAllByStudentIdIn(List<Long> studentIds);

	// endAt이 오늘(today)보다 이전인경우 삭제
	void deleteByEndAtBefore(LocalDate today);
}
