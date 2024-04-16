package org.example.hardauthorization.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.hardauthorization.domain.dto.JwtAuthenticationResponse;
import org.example.hardauthorization.domain.dto.SignInRequest;
import org.example.hardauthorization.domain.dto.SignUpRequest;
import org.example.hardauthorization.domain.model.Role;
import org.example.hardauthorization.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${flag}")
    private String flag;
    @Value("${admin.password}")
    private String adminPassword;


    @PostConstruct
    public void init() {
        User admin = User.builder()
                .username("Admin")
                .password(passwordEncoder.encode(adminPassword))
                .email("admin@mail.ru")
                .flag(flag)
                .role(Role.ROLE_ADMIN)
                .build();
        userService.save(admin);
    }


    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
