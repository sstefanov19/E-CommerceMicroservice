package org.example.userservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.TokenPair;
import org.example.userservice.dto.RegisterRequest;
import org.example.userservice.models.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Transactional
    public void registerUser(
            RegisterRequest request
    ) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("User already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(request.getBalance())
                .build();

        userRepository.save(user);
    }

    public TokenPair loginUser(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateTokenPair(authentication);
    }

    public BigDecimal getUserBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return user != null ? user.getBalance() : new BigDecimal("0.0");
    }

    public Boolean validateToken(String token) {
        return jwtService.isValidToken(token);
    }

}
