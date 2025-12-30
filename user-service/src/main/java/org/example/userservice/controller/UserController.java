package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.RegisterRequest;
import org.example.userservice.dto.TokenPair;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid  @RequestBody  RegisterRequest request) {
        userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPair> loginUser(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = userService.loginUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(tokenPair);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        return userService.validateToken(token);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getUserBalance() {

        BigDecimal balance = userService.getUserBalance();

        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

}
