package org.example.domain.resultannouncer;

import org.example.domain.resultchecker.ResultCheckerFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ResultAnnouncerConfiguration {
    @Bean
    ResultAnnouncerFacade createForTest(ResultCheckerFacade resultCheckerFacade, ResponseRepository responseRepository, Clock clock) {
        return new ResultAnnouncerFacade(resultCheckerFacade, responseRepository, clock);
    }
}
