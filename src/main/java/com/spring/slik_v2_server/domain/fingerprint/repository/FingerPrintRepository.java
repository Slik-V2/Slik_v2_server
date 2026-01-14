package com.spring.slik_v2_server.domain.fingerprint.repository;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FingerPrintRepository extends JpaRepository<FingerPrint, Long> {

	Optional<FingerPrint> findById(Long id);
	Boolean existsByStudentId(String student_id);

	Optional<FingerPrint> findByStudentId(String studentId);
}
