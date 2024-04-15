package com.example.securityservice.dto.mapper;

import com.example.securityservice.dto.user.AuthenticateUserRequest;
import com.example.securityservice.dto.user.AuthenticateUserResponse;
import com.example.securityservice.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(AuthenticateUserRequest dto);

    AuthenticateUserResponse mapToResponse(User user, String access);

}
