package org.example.domain.loginandregister;


import org.example.domain.loginandregister.dto.RegisterUserDto;

class UserMapper {
    public static User mapFromUserDto(RegisterUserDto registerUserDto) {
        return User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
    }
}
