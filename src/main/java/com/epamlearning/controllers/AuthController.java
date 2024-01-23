package com.epamlearning.controllers;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.RefreshRequestDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Controller for managing authentication")
public class AuthController implements BaseController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Login", description = "Login with username and password. Token will be generated and returned. Token expiration time is 30 minutes.")
    @PostMapping("/login")
    public ResponseEntity<SessionDTO> login(@Validated @RequestBody UserAuthDTO userAuthDTO) {
        userService.authenticate(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        return new ResponseEntity<>(generateTokens(userAuthDTO.getUsername()), HttpStatus.OK);
    }

    @Operation(summary = "Refresh token", description = "Refresh token. New tokens (access, refresh) will be generated and returned. Token expiration time is 30 minutes.")
    @PostMapping("/refresh")
    public ResponseEntity<SessionDTO> refresh(@Validated @RequestBody RefreshRequestDTO refreshRequestDTO) {
        String refreshToken = refreshRequestDTO.refreshToken();
        String username = jwtUtil.extractUsername(refreshToken);
        if (jwtUtil.validateToken(refreshToken)) {
            return new ResponseEntity<>(generateTokens(username), HttpStatus.OK);
        } else {
            throw new NotAuthenticated("Invalid refresh token.");
        }
    }

    @Operation(summary = "Change password", description = "Change password for logged in user. Username and old password must be provided.")
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        userService.updatePassword(userChangePasswordDTO.username(), userChangePasswordDTO.oldPassword(), userChangePasswordDTO.newPassword());
        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }

    public SessionDTO generateTokens(String username) {
        // Generate JWT tokens
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // Set expiration times
        Date accessTokenExpiry = jwtUtil.extractExpirationDateFromToken(accessToken);
        Date refreshTokenExpiry = jwtUtil.extractExpirationDateFromToken(refreshToken);

        // Build and return SessionDTO
        return new SessionDTO(
                accessTokenExpiry, new Date(), accessToken, // AccessToken issued at the current time
                refreshTokenExpiry, new Date(), refreshToken // RefreshToken issued at the current time
        );
    }

}
