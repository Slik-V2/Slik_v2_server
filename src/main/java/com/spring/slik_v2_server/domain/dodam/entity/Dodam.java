package com.spring.slik_v2_server.domain.dodam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@Table(name = "dodam")
@NoArgsConstructor
@AllArgsConstructor
public class Dodam {

	@Id
	private long studentId;

	private LocalDate startAt;
	private LocalDate endAt;
}
