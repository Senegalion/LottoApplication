package org.example;

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
public class IntegrationConfiguration {
    @Bean
    @Primary
    AdjustableClock clock() {
        LocalDate date = LocalDate.of(2025, 3, 5);
        LocalTime time = LocalTime.of(12, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        return AdjustableClock.ofLocalDateAndLocalTime(date, time, zone);
    }
}
