package com.stefan.egovernmentapp.models.requests;

import com.stefan.egovernmentapp.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String emailAddress;
    private String password;
    private Boolean isUsing2FA;
    private Role role;
    private String name;
    private String address;
}
