package com.github.vssavin.usmancore.spring5.user;

import com.github.vssavin.usmancore.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to convert a user entity to a corresponding data transfer object and vice versa.
 *
 * @author vssavin on 06.12.2023.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "accountLocked", expression = "java(!user.isAccountNonLocked())")
    @Mapping(target = "credentialsExpired", expression = "java(!user.isCredentialsNonExpired())")
    UserDto toDto(User user);

    @Mapping(target = "accountLocked", expression = "java(userDto.isAccountLocked())")
    @Mapping(target = "credentialsExpired", expression = "java(!userDto.isCredentialsExpired())")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authority", ignore = true)
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "verificationId", ignore = true)
    User toEntity(UserDto userDto);

}
