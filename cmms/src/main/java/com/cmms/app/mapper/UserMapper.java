package com.cmms.app.mapper;

import com.cmms.app.dto.user.request.UserCreateRequest;
import com.cmms.app.dto.user.request.UserUpdateRequest;
import com.cmms.app.dto.user.response.UserResponse;
import com.cmms.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest userCreateRequest);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
