package com.challenge.softschedugarden.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.challenge.softschedugarden.auth.*;
import com.challenge.softschedugarden.entities.TeamMember;
import com.challenge.softschedugarden.enums.Role;
import com.challenge.softschedugarden.repositories.TeamMemberRepository;
import com.challenge.softschedugarden.security.JWTGenerator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceTest {

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTGenerator jwtGenerator;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationService = new AuthenticationService(
                teamMemberRepository,
                passwordEncoder,
                authenticationManager,
                jwtGenerator
        );
    }

    @Test
    void testValidRegistration() {
        // Arrange
        RegisterRequest validRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        TeamMember savedTeamMember = TeamMember.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        when(teamMemberRepository.save(any(TeamMember.class))).thenReturn(savedTeamMember);

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // Act
        RegisterResponse response = authenticationService.register(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());

        // Verify that repository.save() is called
        verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
    }



    @Test
    void testInvalidEmailFormat() {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("invalid-email")
                .password("password")
                .role(Role.USER)
                .build();

        // Act and Assert
        assertThrows(ValidationException.class, () -> authenticationService.register(invalidRequest));
    }

    @Test
    void testNullOrEmptyProperties() {
        // Arrange
        RegisterRequest nullPropertiesRequest = RegisterRequest.builder()
                .name(null)
                .email("john.doe@example.com")
                .password("")
                .role(null)
                .build();

        // Act and Assert
        assertThrows(ValidationException.class, () -> authenticationService.register(nullPropertiesRequest));
    }



    @Test
    public void testAuthenticate() {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testGetAuthenticatedUserEmailWhenNotAuthenticated() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(null);

        // Act
        String userEmail = authenticationService.getAuthenticatedUserEmail();

        // Assert
        assertNull(userEmail);
    }
}

