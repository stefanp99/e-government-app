package com.stefan.egovernmentapp.dtos;

import com.stefan.egovernmentapp.models.Role;
import com.stefan.egovernmentapp.models.User;
import lombok.Builder;

@Builder
public record UserDto(Integer id,
                      String emailAddress,
                      Role role) {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .emailAddress(user.getEmailAddress())
                .role(user.getRole())
                .build();
    }
}
