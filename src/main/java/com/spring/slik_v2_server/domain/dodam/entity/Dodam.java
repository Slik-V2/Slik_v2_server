package com.spring.slik_v2_server.domain.dodam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "dodam")
@NoArgsConstructor
@AllArgsConstructor
public class Dodam {

	@Id
	private long studentId;
}
