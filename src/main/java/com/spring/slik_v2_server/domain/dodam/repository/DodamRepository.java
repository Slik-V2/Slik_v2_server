package com.spring.slik_v2_server.domain.dodam.repository;

import com.spring.slik_v2_server.domain.dodam.entity.Dodam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DodamRepository extends JpaRepository<Dodam, Long> {

	List<Dodam> findAllByStudentIdIn(List<Long> studentIds);
}
