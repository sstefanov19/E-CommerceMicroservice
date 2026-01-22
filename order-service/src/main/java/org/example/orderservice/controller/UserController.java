package org.example.orderservice.controller;

import org.example.orderservice.dto.UpdateBalanceRequest;
import org.example.orderservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/balance")
    public ResponseEntity<BigDecimal>  getUserBalance() {
        BigDecimal balance = userService.getUserBalance();

        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @PostMapping("/balance/update")
    public ResponseEntity<String> updateUserBalance(UpdateBalanceRequest request) {
        userService.updateUserBalance(request);

        return ResponseEntity.status(HttpStatus.OK).body("User balance updated successfully");
    }
}
