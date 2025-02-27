package org.example.domain.resultannouncer;

enum ResponseMessage {
    ID_DOES_NOT_EXIST_MESSAGE("Given ticket does not exist"),
    WAIT_MESSAGE("Results are being calculated, please come back later"),
    WIN_MESSAGE("Congratulations, you won!"),
    LOSE_MESSAGE("No luck, try again!"),
    ALREADY_CHECKED("You have already checked your ticket, please come back later");

    final String info;

    ResponseMessage(String info) {
        this.info = info;
    }
}
