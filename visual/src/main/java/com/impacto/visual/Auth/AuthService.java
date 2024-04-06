package com.impacto.visual.Auth;

import com.impacto.visual.User.User;
import com.impacto.visual.User.UserRepository;
import com.impacto.visual.Jwt.JwtService;
import com.impacto.visual.User.Role;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        Integer id = user.getId();
        return AuthResponse.builder()
            .token(token)
            .id(id)
            .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode( request.getPassword()))
            .persontype(request.getPersontype())
            .documenttype(request.getDocumenttype())
            .documentnumber(request.getDocumentnumber())
            .firstname(request.getFirstname())
            .lastname(request.lastname)
            .address(request.getAddress())
            .email(request.getEmail())
            .phone(request.getPhone())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
        
    }

}
