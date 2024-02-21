package com.epamlearning.dtos.user;

import com.epamlearning.dtos.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshRequestDTO(
        @NotNull(message = "Refresh token is required")
        @NotEmpty(message = "Refresh token is required")
        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLndpY2siLCJleHA")
        String refreshToken
) implements BaseDTO {
}
