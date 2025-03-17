package org.example.infrastructure.security.jwt;

import lombok.AllArgsConstructor;
import org.example.domain.register.RegisterFacade;
import org.example.domain.register.dto.UserDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {
    private final RegisterFacade registerFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userFound = registerFacade.findByUsername(username);
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
