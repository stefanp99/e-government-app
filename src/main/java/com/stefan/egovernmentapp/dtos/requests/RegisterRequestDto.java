package com.stefan.egovernmentapp.dtos.requests;

import com.stefan.egovernmentapp.models.Role;
import lombok.Builder;

@Builder
public record RegisterRequestDto(
        String emailAddress,
        String password,
        Boolean isUsing2FA,
        Role role,
        String name,
        String address
) {
}
