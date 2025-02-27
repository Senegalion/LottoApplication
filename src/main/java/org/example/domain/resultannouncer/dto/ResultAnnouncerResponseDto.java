package org.example.domain.resultannouncer.dto;

import lombok.Builder;

@Builder
public record ResultAnnouncerResponseDto(ResponseDto responseDto, String message) {
}
