package com.nisum.challenge.application.mapper;

import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PhoneMapper.class})
public interface UserMapper {

    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "modified", target = "modified")
    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toUser(UserRequest userRequest);
}
