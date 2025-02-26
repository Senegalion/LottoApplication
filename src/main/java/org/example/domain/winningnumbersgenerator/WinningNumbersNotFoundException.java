package org.example.domain.winningnumbersgenerator;

public class WinningNumbersNotFoundException extends RuntimeException {
    public WinningNumbersNotFoundException(String message) {
        super(message);
    }
}
