package com.stefan.egovernmentapp.dtos.requests;


import lombok.Builder;

@Builder
public record LoginRequestDto(
        String emailAddress,
        String password,
        String totpCode
) {
}
