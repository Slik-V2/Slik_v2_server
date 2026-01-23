package com.spring.slik_v2_server.domain.fingerprint.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Table(name = "fingerprint")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FingerPrint {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@JsonProperty("student_id")
	private String studentId; // 학번(선생님의 경우 0000)

	@Column(columnDefinition = "TEXT")
	private String encrypted_template; // AES-256으로 암호화 된 지문 데이터
}