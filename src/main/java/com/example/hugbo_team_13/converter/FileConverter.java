package com.example.hugbo_team_13.converter;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

/**
 * Utility class for converting files between various formats.
 * Supports converting between MultipartFile, byte array, and Base64 String formats.
 */
@Component
public class FileConverter {

    /**
     * Converts a MultipartFile to a byte array.
     *
     * @param file the MultipartFile to convert
     * @return a byte array representation of the file, or null if the file is null or empty
     * @throws RuntimeException if an IOException occurs during conversion
     */
    public static byte[] multipartFileToBytes(MultipartFile file) {
        try {
            return file != null && !file.isEmpty() ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
        }
    }

    /**
     * Converts a byte array to a Base64 encoded string.
     * Useful for displaying binary data, like images, in HTML as a Base64 string.
     *
     * @param bytes the byte array to encode
     * @return a Base64 encoded string, or null if the byte array is null
     */
    public static String bytesToBase64(byte[] bytes) {
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    /**
     * Decodes a Base64 encoded string back to a byte array.
     * Useful for converting Base64 strings back into binary format.
     *
     * @param base64String the Base64 encoded string to decode
     * @return the decoded byte array, or null if the input string is null
     */
    public static byte[] base64ToBytes(String base64String) {
        return base64String != null ? Base64.getDecoder().decode(base64String) : null;
    }
}
