package com.stefan.egovernmentapp.models.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String emailAddress;
    private String password;
    private String totpCode;
}
