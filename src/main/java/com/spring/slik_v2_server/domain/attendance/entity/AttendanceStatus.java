package com.spring.slik_v2_server.domain.attendance.entity;

import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "finger_print_id")
    private FingerPrint fingerPrint;

    @Enumerated(EnumType.STRING)
    private NightStudyStatus status; // 심자 중, 심자 복귀

    private LocalDateTime nightStudy1LastAction;
    private LocalDateTime nightStudy2LastAction;
}
