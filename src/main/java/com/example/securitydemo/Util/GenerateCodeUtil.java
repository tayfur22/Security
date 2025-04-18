package com.example.securitydemo.Util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class GenerateCodeUtil {
    private GenerateCodeUtil() {}

    public static String generateCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int code = random.nextInt(9000) + 1000; // 1000-9999
            return String.valueOf(code);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code.");
        }
    }
}

