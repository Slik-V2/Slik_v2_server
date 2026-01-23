package com.spring.slik_v2_server.domain.attendance.entity;

import com.spring.slik_v2_server.domain.dodam.entity.Type;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.domain.student.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

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

	@Enumerated(EnumType.STRING)
	private Type type; //심자 종류

	private AttendanceStatus s1Status; //심1 상태
	private LocalTime s1InTime; //심1 출석 시간
	private LocalTime s1OutTime; // 심1 퇴실 시간
	private AttendanceStatus s2Status; //심2 상태
	private LocalTime s2InTime; // 심2 출석 시간
	private LocalTime s2OutTime; // 심2 퇴실한 시

	@OneToOne
	private Student student; //학생정보

	public void setEndTime(LocalTime s1OutTime) {
		this.s1OutTime = s1OutTime;
	}

	public String updateAttendanceTime(LocalTime now, AttendanceTimeSet timeSet) {
		if (isTimeInRange(now, timeSet.getSession1_1Start(), timeSet.getSession1_1End())) {
			if (this.s1InTime == null) {
				this.s1InTime = now;
				this.s1Status = AttendanceStatus.STUDYING;
				return "S1_IN";
			} else {
				return "S1_IN_ALREADY";
			}
		}

		else if (isTimeInRange(now, timeSet.getSession1_2Start(), timeSet.getSession1_2End())) {
			if (this.s1OutTime == null && this.s1InTime != null) {
				this.s1OutTime = now;
				this.s1Status = AttendanceStatus.RETURNED;
				return "S1_OUT";
			} else if (this.s1InTime == null) {
				return "S1_OUT_NO_IN";
			} else {
				return "S1_OUT_ALREADY";
			}
		}

		else if (isTimeInRange(now, timeSet.getSession2_1Start(), timeSet.getSession2_1End())) {
			if (this.s1OutTime != null && this.s2InTime == null) {
				this.s2InTime = now;
				this.s2Status = AttendanceStatus.STUDYING;
				return "S2_IN";
			} else if (this.s1OutTime == null) {
				return "S2_IN_NO_S1_OUT";
			} else {
				return "S2_IN_ALREADY";
			}
		}

		else if (isTimeInRange(now, timeSet.getSession2_2Start(), timeSet.getSession2_2End())) {
			if (this.s1OutTime != null && this.s2OutTime == null && this.s2InTime != null) {
				this.s2OutTime = now;
				this.s2Status = AttendanceStatus.RETURNED;
				return "S2_OUT";
			} else if (this.s2InTime == null) {
				return "S2_OUT_NO_IN";
			} else {
				return "S2_OUT_ALREADY";
			}
		}

		return "NO_UPDATE";
	}

	private boolean isTimeInRange(LocalTime now, LocalTime start, LocalTime end) {
		return (now.isAfter(start) || now.equals(start)) && (now.isBefore(end) || now.equals(end));
	}
}
