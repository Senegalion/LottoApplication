package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.numberreceiver.AdjustableClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
@Profile("integration")
@Slf4j
public class IntegrationConfiguration {
    @Bean
    @Primary
    AdjustableClock clock() {
        LocalDate date = LocalDate.of(2025, 3, 5);
        LocalTime time = LocalTime.of(11, 0, 0);
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        log.info(AdjustableClock.ofLocalDateAndLocalTime(date, time, zone).toString());
        return AdjustableClock.ofLocalDateAndLocalTime(date, time, zone);
    }
}
