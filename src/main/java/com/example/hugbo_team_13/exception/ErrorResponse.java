package com.example.hugbo_team_13.exception;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a standardized error response sent to the client.
 * This class encapsulates the HTTP status code and an error message.
 */
@Data
@Builder
public class ErrorResponse {

    /**
     * The HTTP status code associated with the error.
     */
    private int statusCode;

    /**
     * A descriptive error message detailing the issue.
     */
    private String message;
}
