package org.example.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginAndRegisterConfiguration {
    @Bean
    LoginAndRegisterFacade createForTest(UserRepository userRepository) {
        return new LoginAndRegisterFacade(userRepository);
    }
}
