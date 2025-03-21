package org.example.domain.numberreceiver;

enum ValidationResult {
    NOT_SIX_NUMBERS_GIVEN("YOU SHOULD GIVE 6 NUMBERS"),
    NOT_IN_RANGE("YOUR NUMBERS ARE NOT IN RANGE"),
    INPUT_SUCCESS("SUCCESS");

    final String info;

    ValidationResult(String info) {
        this.info = info;
    }
}
