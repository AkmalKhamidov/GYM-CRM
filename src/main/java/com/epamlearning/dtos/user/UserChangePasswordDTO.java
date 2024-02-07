package com.epamlearning.dtos.user;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.epamlearning.dtos.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserChangePasswordDTO(
        @Schema(description = "User username", example = "John.Wick", requiredMode = REQUIRED)
        @NotNull(message = "Username is required")
        @NotEmpty(message = "Username is required")
        String username,

        @Schema(description = "User old password", example = "old password", requiredMode = REQUIRED)
        @NotNull(message = "Old password is required")
        @NotBlank(message = "Old password is required")
        @Size(min = 8, max = 20, message = "Old password must be between 8 and 20 characters long")
        String oldPassword,

        @Schema(description = "User new password", example = "new password", requiredMode = REQUIRED)
        @NotNull(message = "New password is required")
        @NotBlank(message = "New password is required")
        @Size(min = 8, max = 20, message = "New password must be between 8 and 20 characters long")
        String newPassword) implements BaseDTO {
}
