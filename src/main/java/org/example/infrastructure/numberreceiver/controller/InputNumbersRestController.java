package org.example.infrastructure.numberreceiver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class InputNumbersRestController {
    @PostMapping("/inputNumbers")
    public ResponseEntity<String> inputNumbers(@RequestBody InputNumbersRequestDto requestDto) {
        log.info("Number from user: " + requestDto.inputNumbers().toString());
        return ResponseEntity.ok("hehe");
    }
}
