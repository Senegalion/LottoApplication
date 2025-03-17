package org.example.domain.register;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegisterConfiguration {
    @Bean
    RegisterFacade createForTest(UserRepository userRepository) {
        return new RegisterFacade(userRepository);
    }
}
