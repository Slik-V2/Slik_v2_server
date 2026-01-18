package com.spring.slik_v2_server.domain.attendance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceTimeSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate today;

	// 심자1 출석체크 가능 범위
	private LocalTime session1_1Start;
	private LocalTime session1_1End;

	// 심자1 복귀체크 가능 범위
	private LocalTime session1_2Start;
	private LocalTime session1_2End;

	// 심자2 출석체크 가능 범위
	private LocalTime session2_1Start;
	private LocalTime session2_1End;

	// 심자2 복귀체크 가능 범위
	private LocalTime session2_2Start;
	private LocalTime session2_2End;

	public void updateTime(LocalTime session1_1Start,
						   LocalTime session1_1End,
						   LocalTime session1_2Start,
						   LocalTime session1_2End,
						   LocalTime session2_1Start,
						   LocalTime session2_1End,
						   LocalTime session2_2Start,
						   LocalTime session2_2End) {
		this.session1_1Start = session1_1Start;
		this.session1_1End = session1_1End;
		this.session1_2Start = session1_2Start;
		this.session1_2End = session1_2End;
		this.session2_1Start = session2_1Start;
		this.session2_1End = session2_1End;
		this.session2_2Start = session2_2Start;
		this.session2_2End = session2_2End;
	}
}
