package com.example.hugbo_team_13.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data Transfer Object for handling file uploads.
 * Encapsulates the uploaded file, optional metadata such as type and description.
 */
@Data
public class FileUploadDTO {

    /**
     * The uploaded file as a MultipartFile object.
     */
    private MultipartFile file;

    /**
     * An optional string to identify the type of upload.
     */
    private String type;

    /**
     * An optional description providing metadata about the upload.
     */
    private String description;
}
