package org.example.domain.resultchecker.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PlayersDto(List<ResultDTO> results, String message) {
}
