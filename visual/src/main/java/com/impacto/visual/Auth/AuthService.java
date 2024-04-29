package com.impacto.visual.Auth;

import com.impacto.visual.User.User;
import com.impacto.visual.User.UserRepository;
import com.impacto.visual.Jwt.JwtService;
import com.impacto.visual.User.Role;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
        
        // Intentar encontrar el usuario por su nombre de usuario
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        
        // Verificar si se encontró un usuario
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = jwtService.getToken(user);
            Integer id = user.getId();
            return AuthResponse.builder()
                .token(token)
                .id(id)
                .build();
        } else {
            // Si no se encuentra el usuario, lanzar una excepción de autenticación
            throw new BadCredentialsException("Usuario no encontrado");
        }
    }
    public AuthResponse register(RegisterRequest request) {
        String password = request.getPassword();

        // Validar la contraseña
        if (password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        }
    
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(password))
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
