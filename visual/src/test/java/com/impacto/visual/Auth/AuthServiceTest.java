package com.impacto.visual.Auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.impacto.visual.User.UserRepository;
import com.impacto.visual.Jwt.JwtService;
import com.impacto.visual.User.User;
import com.impacto.visual.User.Role;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, jwtService, passwordEncoder, authenticationManager);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest("username", "password");
        User user = new User(1, "username", "password", null, null, null, null, null, null, null, null, Role.USER);
        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user));
        String token = "mockedToken";
        when(jwtService.getToken(user)).thenReturn(token);

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertEquals(token, response.getToken());
        assertEquals(user.getId(), response.getId());
    }

    @Test
    void testLoginFailure() {
        // Arrange
        LoginRequest request = new LoginRequest("username", "wrongPassword");
        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.empty());
        
        // Act and Assert
        assertThrows(AuthenticationException.class, () -> authService.login(request));
    }
}