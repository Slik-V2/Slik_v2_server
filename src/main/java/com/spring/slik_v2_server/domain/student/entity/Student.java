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
    @Builder.Default
    private int totalDate = 0;
    @Builder.Default
    private int attendanceDate = 0;

    @OneToOne
    private FingerPrint fingerPrint;

    public void updateFingerPrint(FingerPrint fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public void plusDate() {
        this.totalDate++;
    }

    public void increaseAttendanceDate() {
        this.attendanceDate++;
    }
}
