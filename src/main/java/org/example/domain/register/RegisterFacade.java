package org.example.domain.register;

import lombok.AllArgsConstructor;
import org.example.domain.register.dto.RegisterUserDto;
import org.example.domain.register.dto.RegistrationResultDto;
import org.example.domain.register.dto.UserDto;
import org.springframework.security.authentication.BadCredentialsException;

@AllArgsConstructor
public class RegisterFacade {
    private final UserRepository userRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        if (registerUserDto.username() == null || registerUserDto.password() == null) {
            throw new InvalidUserCredentialsException("Both username and password cannot be null");
        }
        User user = UserMapper.mapFromUserDto(registerUserDto);
        User savedUser = userRepository.save(user);
        return RegistrationResultDto.builder()
                .userId(savedUser.userId())
                .wasCreated(true)
                .username(registerUserDto.username())
                .build();
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto(user.userId(), user.username(), user.password()))
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
}
