package com.spring.slik_v2_server.domain.attendance.entity;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@SuperBuilder
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
