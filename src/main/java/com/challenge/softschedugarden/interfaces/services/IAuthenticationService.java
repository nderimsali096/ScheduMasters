package com.challenge.softschedugarden.interfaces.services;

import com.challenge.softschedugarden.auth.AuthenticationRequest;
import com.challenge.softschedugarden.auth.AuthenticationResponse;
import com.challenge.softschedugarden.auth.RegisterRequest;
import com.challenge.softschedugarden.auth.RegisterResponse;

public interface IAuthenticationService {
    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    String getAuthenticatedUserEmail();
}

