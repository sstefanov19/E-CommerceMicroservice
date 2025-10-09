package org.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username shouldn't be empty!")
    @Size(min = 3 , max = 12 , message = "Username should be between 3 and 12 characters")
    private String username;

    @NotBlank(message = "Password shouldn't be empty!")
    @Size(min = 6 , max = 14 , message = "Password should be between 6 and 14 characters")
    private String password;
}
