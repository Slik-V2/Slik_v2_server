package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.attendance.entity.AttendanceTime;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceType;
import com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceRepository;
import com.spring.slik_v2_server.domain.attendance.repository.AttendanceStatusRepository;
import com.spring.slik_v2_server.domain.device.dto.response.VerificationResponse;
import com.spring.slik_v2_server.domain.device.exception.DeviceStatusCode;
import com.spring.slik_v2_server.domain.device.dto.request.DeviceRequest;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final SimpMessagingTemplate messagingTemplate;
    private final TextEncryptor textEncryptor;
    private final DecryptService decryptService;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceStatusRepository attendanceStatusRepository;

    @Value("${spring.security.KEY}")
    private String secertKey;

    public ApiResponse<String> Verification(DeviceRequest request, HttpServletRequest servletRequest) {
        if (!secertKey.equals(servletRequest.getHeader("X-API-KEY"))) {
            throw new ApplicationException(DeviceStatusCode.INVALID_API_KEY);
        }
        String pingerdata = textEncryptor.decrypt(request.encrypted_template());
        FingerPrint student = decryptService.ContrastRatioComparator(pingerdata);

        AttendanceType attendanceType = getAttendanceType(LocalTime.now());

        attendanceStatusRepository.save(com.spring.slik_v2_server.domain.attendance.entity.AttendanceStatus.builder()
                .date(LocalDateTime.now())
                .fingerPrint(student)
                .attendanceType(attendanceType)
                .build());

        messagingTemplate.convertAndSend(
                "/topic/" + request.device_id(),
                new VerificationResponse(
                        student != null,
                        student != null ? student.getName() + " 출석 확인했습니다." : "일치한 정보가 없습니다."
                )
        );
        return ApiResponse.ok("서버 수신 성공");
    }

    public AttendanceType getAttendanceType(LocalTime currentTime) {
        List<AttendanceTime> times = attendanceRepository.findAll();

        for(AttendanceTime time : times) {
            LocalTime start = time.getStartTime();
            LocalTime end = time.getEndTime();

            if (!currentTime.isBefore(start) && !currentTime.isAfter(end)) {
                return time.getType();
            }
        }
        throw new ApplicationException(com.spring.slik_v2_server.domain.attendance.exception.AttendanceStatus.NOT_IN_ATTENDANCE_TIME);
    }
}
