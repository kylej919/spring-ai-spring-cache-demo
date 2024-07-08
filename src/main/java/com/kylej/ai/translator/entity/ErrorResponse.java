package com.kylej.ai.translator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * A packaged API response for error messages to display to the caller
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private String message;
}
