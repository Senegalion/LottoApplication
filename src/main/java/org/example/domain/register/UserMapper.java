package org.example.domain.register;


import org.example.domain.register.dto.RegisterUserDto;

class UserMapper {
    public static User mapFromUserDto(RegisterUserDto registerUserDto) {
        return User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
    }
}
