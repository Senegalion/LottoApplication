package org.example.infrastructure.resultannouncer.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.resultchecker.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ResultAnnouncerControllerErrorHandler {
    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultAnnouncerErrorResponse resultAnnouncerErrorResponse(PlayerNotFoundException playerNotFoundException) {
        String errorMessage = playerNotFoundException.getMessage();
        log.error(errorMessage);
        return ResultAnnouncerErrorResponse.builder()
                .message(errorMessage)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
