package com.epamlearning.dtos.user;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Old password is required")
        String oldPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        String newPassword) implements BaseDTO {
}
