package com.spring.slik_v2_server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class CryptoConfig {
    @Value("${spring.security.KEY}")
    private String key;

    @Bean
    public TextEncryptor textEncryptor() {
        return new AesTextEncryptor(key);
    }

    private static class AesTextEncryptor implements TextEncryptor {
        private final SecretKeySpec secretKey;
        private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

        public AesTextEncryptor(String key) {
            this.secretKey = new SecretKeySpec(key.getBytes(), "AES");
        }

        @Override
        public String encrypt(String text) {
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encrypted = cipher.doFinal(text.getBytes());
                return Base64.getEncoder().encodeToString(encrypted);
            } catch (Exception e) {
                throw new RuntimeException("Encryption failed", e);
            }
        }

        @Override
        public String decrypt(String encryptedText) {
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decoded = Base64.getDecoder().decode(encryptedText);
                byte[] decrypted = cipher.doFinal(decoded);
                return new String(decrypted);
            } catch (Exception e) {
                throw new RuntimeException("Decryption failed", e);
            }
        }
    }
}
