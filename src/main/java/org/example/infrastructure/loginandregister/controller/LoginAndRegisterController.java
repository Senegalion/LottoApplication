package org.example.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.example.domain.register.RegisterFacade;
import org.example.domain.register.dto.RegisterUserDto;
import org.example.domain.register.dto.RegistrationResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class LoginAndRegisterController {
    private final RegisterFacade registerFacade;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        String encodedPassword = passwordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registrationResultDto =
                registerFacade.register(
                        new RegisterUserDto(registerUserDto.username(), encodedPassword)
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResultDto);
    }
}
