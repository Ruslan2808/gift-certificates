package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

}
