package com.spring.slik_v2_server.domain.attendance.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;

import com.spring.slik_v2_server.domain.student.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //기본키

	@OneToOne
	private FingerPrint fingerPrint; //학생의 지문 정보

	private LocalDate today; //날짜

	private AttendanceStatus s1Status; //심1 상태
	@Builder.Default
	private LocalTime s1InTime = LocalTime.now(); //심1 출석 시간
	private LocalTime s1OutTime; // 심1 퇴실 시간
	private AttendanceStatus s2Status; //심2 상태
	private LocalTime s2InTime; // 심2 출석 시간
	private LocalTime s2OutTime; // 심2 퇴실한 시

	@OneToOne
	private Student student;

	public void setEndTime(LocalTime s1OutTime) {
		this.s1OutTime = s1OutTime;
	}

	public void updateAttendanceTime(LocalTime now, AttendanceTimeSet timeSet) {
		// Session 1-1: 출석 (S1InTime)
		if (isTimeInRange(now, timeSet.getSession1_1Start(), timeSet.getSession1_1End())) {
			if (this.s1InTime == null) {
				this.s1InTime = now;
				this.s1Status = AttendanceStatus.STUDYING;
			}
		}
		// Session 1-2: 복귀 (S1OutTime)
		else if (isTimeInRange(now, timeSet.getSession1_2Start(), timeSet.getSession1_2End())) {
			if (this.s1OutTime == null && this.s1InTime != null) {
				this.s1OutTime = now;
				this.s1Status = AttendanceStatus.STUDYING;
			}
		}
		// Session 2-1: 출석 (S2InTime) - S1OutTime이 null이면 진행 불가
		else if (isTimeInRange(now, timeSet.getSession2_1Start(), timeSet.getSession2_1End())) {
			if (this.s1OutTime != null && this.s2InTime == null) {
				this.s2InTime = now;
				this.s2Status = AttendanceStatus.STUDYING;
			}
		}
		// Session 2-2: 복귀 (S2OutTime) - S1OutTime이 null이면 진행 불가
		else if (isTimeInRange(now, timeSet.getSession2_2Start(), timeSet.getSession2_2End())) {
			if (this.s1OutTime != null && this.s2OutTime == null && this.s2InTime != null) {
				this.s2OutTime = now;
				this.s2Status = AttendanceStatus.STUDYING;
			}
		}
	}

	private boolean isTimeInRange(LocalTime now, LocalTime start, LocalTime end) {
		return (now.isAfter(start) || now.equals(start)) && (now.isBefore(end) || now.equals(end));
	}
}
