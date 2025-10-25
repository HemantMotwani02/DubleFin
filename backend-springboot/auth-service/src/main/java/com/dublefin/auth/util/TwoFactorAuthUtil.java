package com.dublefin.auth.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TwoFactorAuthUtil {

    private static final String ISSUER = "DubleFin Banking";
    private static final int SECRET_SIZE = 20;

    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[SECRET_SIZE];
        random.nextBytes(bytes);
        return Base32.encode(bytes);
    }

    public String generateQRCodeUrl(String email, String secret) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
                ISSUER, email, secret, ISSUER);
    }

    public byte[] generateQRCodeImage(String qrCodeUrl, int width, int height) 
            throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(qrCodeUrl, BarcodeFormat.QR_CODE, width, height);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    public boolean verifyCode(String secret, String code) {
        try {
            long timeWindow = System.currentTimeMillis() / 1000 / 30;
            
            // Check current window and adjacent windows for clock skew
            for (int i = -1; i <= 1; i++) {
                String generatedCode = generateTOTP(secret, timeWindow + i);
                if (generatedCode.equals(code)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateTOTP(String secret, long timeWindow) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] key = Base32.decode(secret);
        byte[] data = new byte[8];
        long value = timeWindow;
        for (int i = 7; value > 0; i--) {
            data[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[hash.length - 1] & 0x0F;
        int truncatedHash = 0;
        for (int i = 0; i < 4; i++) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return String.format("%06d", truncatedHash);
    }

    // Simple Base32 implementation
    private static class Base32 {
        private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

        public static String encode(byte[] data) {
            StringBuilder result = new StringBuilder();
            int buffer = 0;
            int bufferLength = 0;

            for (byte b : data) {
                buffer = (buffer << 8) | (b & 0xFF);
                bufferLength += 8;

                while (bufferLength >= 5) {
                    result.append(BASE32_CHARS.charAt((buffer >> (bufferLength - 5)) & 0x1F));
                    bufferLength -= 5;
                }
            }

            if (bufferLength > 0) {
                result.append(BASE32_CHARS.charAt((buffer << (5 - bufferLength)) & 0x1F));
            }

            return result.toString();
        }

        public static byte[] decode(String base32) {
            base32 = base32.toUpperCase();
            byte[] result = new byte[base32.length() * 5 / 8];
            int buffer = 0;
            int bufferLength = 0;
            int index = 0;

            for (char c : base32.toCharArray()) {
                int value = BASE32_CHARS.indexOf(c);
                if (value < 0) continue;

                buffer = (buffer << 5) | value;
                bufferLength += 5;

                if (bufferLength >= 8) {
                    result[index++] = (byte) (buffer >> (bufferLength - 8));
                    bufferLength -= 8;
                }
            }

            return result;
        }
    }
}

