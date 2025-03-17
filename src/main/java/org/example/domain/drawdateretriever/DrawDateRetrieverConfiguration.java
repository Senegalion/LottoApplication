package org.example.domain.drawdateretriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class DrawDateRetrieverConfiguration {
    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    DrawDateRetrieverFacade drawDateRetrieverFacade(Clock clock) {
        DrawDateGeneratorService drawDateGeneratorService = new DrawDateGeneratorService(clock);
        return new DrawDateRetrieverFacade(drawDateGeneratorService);
    }
}
