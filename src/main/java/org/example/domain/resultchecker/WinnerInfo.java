package org.example.domain.resultchecker;

public enum WinnerInfo {
    NUMBERS_WHEN_PLAYER_WON(3);

    final int number;

    WinnerInfo(int number) {
        this.number = number;
    }
}
