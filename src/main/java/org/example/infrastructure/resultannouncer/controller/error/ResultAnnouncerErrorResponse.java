package org.example.infrastructure.resultannouncer.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ResultAnnouncerErrorResponse(
        String message,
        HttpStatus status
) {
}
