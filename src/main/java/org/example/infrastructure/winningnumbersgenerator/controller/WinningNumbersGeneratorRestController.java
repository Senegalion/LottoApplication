package org.example.infrastructure.winningnumbersgenerator.controller;

import lombok.AllArgsConstructor;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/winningNumbers")
@AllArgsConstructor
public class WinningNumbersGeneratorRestController {
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @GetMapping
    public ResponseEntity<WinningNumbersDto> getWinningNumbers() {
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        return ResponseEntity.ok(winningNumbersDto);
    }
}
