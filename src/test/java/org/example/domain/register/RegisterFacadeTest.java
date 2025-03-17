package org.example.domain.register;

import org.example.domain.register.dto.RegisterUserDto;
import org.example.domain.register.dto.RegistrationResultDto;
import org.example.domain.register.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterFacadeTest {
    private final UserRepository userRepository = new UserRepositoryTestImpl();

    @Test
    public void shouldRegisterUser() {
        RegisterFacade registerFacade = new RegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();
        String testPassword = TestCredentials.getPassword();
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);

        RegistrationResultDto register = registerFacade.register(registerUserDto);

        assertAll(
                () -> assertThat(register.wasCreated()).isTrue(),
                () -> assertThat(register.username()).isEqualTo(testUsername)
        );
    }

    @Test
    public void shouldThrowExceptionWhenUserEntersUsernameThatWasNull() {
        RegisterFacade registerFacade = new RegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = null;
        String testPassword = TestCredentials.getPassword();
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);

        assertThrows(InvalidUserCredentialsException.class, () -> registerFacade.register(registerUserDto));
    }

    @Test
    public void shouldThrowExceptionWhenUserEntersPasswordThatWasNull() {
        RegisterFacade registerFacade = new RegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();
        String testPassword = null;
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);

        assertThrows(InvalidUserCredentialsException.class, () -> registerFacade.register(registerUserDto));
    }

    @Test
    public void shouldFindUserByUsername() {
        RegisterFacade registerFacade = new RegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();
        String testPassword = TestCredentials.getPassword();
        RegisterUserDto registerUserDto = new RegisterUserDto(testUsername, testPassword);
        RegistrationResultDto registrationResultDto = registerFacade.register(registerUserDto);

        UserDto foundUser = registerFacade.findByUsername(testUsername);

        assertThat(foundUser).isEqualTo(new UserDto(registrationResultDto.userId(), testUsername, testPassword));
    }

    @Test
    public void shouldThrowExceptionWhenUserHasNotBeenFound() {
        RegisterFacade registerFacade = new RegisterConfiguration()
                .createForTest(userRepository);
        String testUsername = TestCredentials.getUsername();

        assertThrows(BadCredentialsException.class, () -> registerFacade.findByUsername(testUsername));
    }
}