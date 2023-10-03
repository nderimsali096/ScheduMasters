package com.challenge.softschedugarden.auth;

import com.challenge.softschedugarden.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private String name;
    private String email;
    private Role role;
}
