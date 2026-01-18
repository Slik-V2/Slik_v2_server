package com.spring.slik_v2_server.domain.student.entity;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String name;

    @OneToOne
    private FingerPrint fingerPrint;

    public void updateFingerPrint(FingerPrint fingerPrint) {
        this.fingerPrint = fingerPrint;
    }
}
