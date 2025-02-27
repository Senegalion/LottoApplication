package org.example.domain.resultchecker;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message, String id) {
        super(message);
    }
}
