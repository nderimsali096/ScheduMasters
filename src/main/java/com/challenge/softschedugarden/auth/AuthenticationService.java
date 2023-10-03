package com.challenge.softschedugarden.auth;

import com.challenge.softschedugarden.entities.TeamMember;
import com.challenge.softschedugarden.exceptions.AuthenticationFailureException;
import com.challenge.softschedugarden.interfaces.services.IAuthenticationService;
import com.challenge.softschedugarden.repositories.TeamMemberRepository;
import com.challenge.softschedugarden.security.JWTGenerator;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final TeamMemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public RegisterResponse register(RegisterRequest request) {
        if (!isValidEmail(request.getEmail())) {
            throw new ValidationException("Invalid email format");
        }

        if (StringUtils.isBlank(request.getName()) ||
                StringUtils.isBlank(request.getPassword()) ||
                request.getRole() == null) {
            throw new ValidationException("Name, password, and role must not be null or empty");
        }

        var teamMember = TeamMember.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        var savedTeamMember = repository.save(teamMember);

        return RegisterResponse.builder()
                .name(savedTeamMember.getName())
                .email(savedTeamMember.getEmail())
                .role(savedTeamMember.getRole())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtGenerator.generateToken(authentication);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException ex) {
            throw new AuthenticationFailureException("Authentication failed: " + ex.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    @Override
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
