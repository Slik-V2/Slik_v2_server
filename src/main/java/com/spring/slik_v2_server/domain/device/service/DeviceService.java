package com.spring.slik_v2_server.domain.device.service;

import com.spring.slik_v2_server.domain.device.dto.response.VerificationResponse;
import com.spring.slik_v2_server.domain.device.exception.DeviceStatusCode;
import com.spring.slik_v2_server.domain.device.dto.request.DeviceRequest;
import com.spring.slik_v2_server.domain.fingerprint.entity.FingerPrint;
import com.spring.slik_v2_server.global.data.ApiResponse;
import com.spring.slik_v2_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final SimpMessagingTemplate messagingTemplate;
    private final TextEncryptor textEncryptor;
    private DecryptService decryptService;

    @Value("${spring.security.KEY}")
    private String secertKey;

    public ApiResponse<?> Verification(DeviceRequest request, String apiKey) {
        if (!secertKey.equals(apiKey)) {
            throw new ApplicationException(DeviceStatusCode.INVALID_API_KEY);
        }
        String pingerdata = textEncryptor.decrypt(request.encrypted_template());
        FingerPrint student = decryptService.ContrastRatioComparator(pingerdata);

        messagingTemplate.convertAndSend(
                "/topic/" + request.device_id(),
                new VerificationResponse(
                        student != null,
                        student != null ? student.getName() + " 출석 확인했습니다." : "일치한 정보가 없습니다."
                )

        );
        return ApiResponse.ok("서버 수신 성공");
    }
}
