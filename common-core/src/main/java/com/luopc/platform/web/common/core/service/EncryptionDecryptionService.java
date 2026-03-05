package com.luopc.platform.web.common.core.service;

import lombok.Setter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class EncryptionDecryptionService {

    @Setter
    private StringEncryptor stringEncryptor;

    public String decrypt(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return encryptedValue;
        }
        return stringEncryptor.decrypt(encryptedValue);
    }

    public String encrypt(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return stringEncryptor.encrypt(value);
    }

}
