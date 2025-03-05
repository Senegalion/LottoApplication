package org.example.domain.drawdateretriever;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class DrawDateRetrieverFacade {
    private final DrawDateGeneratorService drawDateGeneratorService;

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGeneratorService.getNextDrawDate();
    }
}
