package com.epamlearning.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record SessionDTO(
        @Schema(description = "Token expiring date", example = "22-01-2024 00:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        Date accessTokenExpiry,
        @Schema(description = "Token created (issued) date", example = "22-01-2024 00:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        Date issuedAt,
        @Schema(description = "Access token", example = "token")
        String accessToken
) {
}
