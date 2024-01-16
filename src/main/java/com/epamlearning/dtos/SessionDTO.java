package com.epamlearning.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record SessionDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        Date accessTokenExpiry,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        Date issuedAt,
        String accessToken
) {
}
