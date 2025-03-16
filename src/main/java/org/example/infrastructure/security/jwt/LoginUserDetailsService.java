package org.example.infrastructure.security.jwt;

import lombok.AllArgsConstructor;
import org.example.domain.loginandregister.LoginAndRegisterFacade;
import org.example.domain.loginandregister.dto.UserDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {
    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userFound = loginAndRegisterFacade.findByUsername(username);
        return getUser(userFound);
    }

    private User getUser(UserDto userDto) {
        return new User(
                userDto.username(),
                userDto.password(),
                Collections.emptyList()
        );
    }
}
