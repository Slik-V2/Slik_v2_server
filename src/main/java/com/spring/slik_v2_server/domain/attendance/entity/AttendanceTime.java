package com.spring.slik_v2_server.domain.attendance.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceTime {

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private AttendanceType type;

	@OneToOne
	private FingerPrint fingerPrint;

	private LocalDate today;
	@Builder.Default
	private LocalTime startTime = LocalTime.now();
	private LocalTime endTime;

	public void updateTime(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void setType(AttendanceType type) {
		this.type = type;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
