package com.spring.slik_v2_server.domain.fingerprint.entity;


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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String studentId; // 학번(선생님의 경우 0000)

	@Column(name = "encrypted_template", columnDefinition = "TEXT")
	private String encryptedTemplate; // AES-256으로 암호화 된 지문 데이터
}