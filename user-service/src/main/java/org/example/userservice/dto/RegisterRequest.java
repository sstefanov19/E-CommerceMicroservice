package org.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 12 , message = "Username must be between 3 and 12 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank!")
    @Email
    @Size(min = 3 , max = 15 , message = "Email should be between 3 and 15 characters!")
    private String email;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 6 , max = 14 , message = "Password should be between 6 and 14 characters")
    private String password;
}
