package com.example.hugbo_team_13.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadDTO {
    private MultipartFile file;
    private String type;  // optional - to identify type of upload
    private String description;  // optional metadata

}
