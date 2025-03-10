package org.example.infrastructure.numberreceiver.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class InputNumbersRestController {
    @Autowired
    NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    public ResponseEntity<NumberReceiverResponseDto> inputNumbers(@RequestBody @Valid InputNumbersRequestDto requestDto) {
        NumberReceiverResponseDto numberReceiverResponseDto =
                numberReceiverFacade.inputNumbers(requestDto.inputNumbers());

        return ResponseEntity.ok(numberReceiverResponseDto);
    }
}
