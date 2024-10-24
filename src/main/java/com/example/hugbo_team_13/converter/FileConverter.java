package com.example.hugbo_team_13.converter;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class FileConverter {

    // Convert MultipartFile to byte[] (for uploads)
    public static byte[] multipartFileToBytes(MultipartFile file) {
        try {
            return file != null && !file.isEmpty() ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
        }
    }

    // Convert byte[] to Base64 String (for displaying in HTML)
    public static String bytesToBase64(byte[] bytes) {
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    // Convert Base64 String back to byte[] (if needed)
    public static byte[] base64ToBytes(String base64String) {
        return base64String != null ? Base64.getDecoder().decode(base64String) : null;
    }
}
