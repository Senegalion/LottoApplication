package org.example.domain.winningnumbersgenerator.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record SixRandomNumbersDto(Set<Integer> numbers) {
}
