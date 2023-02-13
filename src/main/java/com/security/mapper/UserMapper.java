package com.security.mapper;

import com.security.entity.User;
import com.security.entity.dto.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "username", source = "entity.username")
    @Mapping(target = "password", source = "entity.password")
    UserRequestDto userToUserDto(User entity);

    @Mapping(target = "username", source = "entity.username")
    @Mapping(target = "password", source = "entity.password")
    User userRequestDtoToUser(UserRequestDto entity);
}
