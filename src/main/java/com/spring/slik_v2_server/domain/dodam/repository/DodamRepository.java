package com.spring.slik_v2_server.domain.dodam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.slik_v2_server.domain.dodam.entity.Dodam;

public interface DodamRepository extends JpaRepository<Dodam, Long> {

	List<Dodam> findAllByStudentIdIn(List<Long> studentIds);
}
