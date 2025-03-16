package org.example.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.example.domain.loginandregister.dto.RegisterUserDto;
import org.example.domain.loginandregister.dto.RegistrationResultDto;
import org.example.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {
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
