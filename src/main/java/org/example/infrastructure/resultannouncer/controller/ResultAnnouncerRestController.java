package org.example.infrastructure.resultannouncer.controller;

import lombok.AllArgsConstructor;
import org.example.domain.resultannouncer.ResultAnnouncerFacade;
import org.example.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
@AllArgsConstructor
public class ResultAnnouncerRestController {
    ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/{id}")
    public ResponseEntity<ResultAnnouncerResponseDto> checkResultsById(@PathVariable String id) {
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(id);
        return ResponseEntity.ok(resultAnnouncerResponseDto);
    }
}
