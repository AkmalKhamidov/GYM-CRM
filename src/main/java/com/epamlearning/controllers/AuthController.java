package com.epamlearning.controllers;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.RefreshRequestDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Controller for managing authentication")
public class AuthController implements BaseController {

    private final UserServiceImpl userServiceImpl;
    @Autowired
    public AuthController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Operation(summary = "Login", description = "Login with username and password. Token will be generated and returned. Token expiration time is 30 minutes.")
    @PostMapping("/login")
    public ResponseEntity<SessionDTO> login(@Validated @RequestBody UserAuthDTO userAuthDTO) {
        SessionDTO responseDTO = userServiceImpl.authenticate(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Refresh token", description = "Refresh token. New tokens (access, refresh) will be generated and returned. Token expiration time is 30 minutes.")
    @PostMapping("/refresh")
    public ResponseEntity<SessionDTO> refresh(@Validated @RequestBody RefreshRequestDTO refreshRequestDTO) {
        return new ResponseEntity<>(userServiceImpl.refreshToken(refreshRequestDTO), HttpStatus.OK);
    }

    @PreAuthorize("principal.username == #userChangePasswordDTO.username and principal.enabled == true")
    @Operation(summary = "Change password", description = "Change password for logged in user. Username and old password must be provided.")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        userServiceImpl.updatePassword(userChangePasswordDTO.username(), userChangePasswordDTO.oldPassword(), userChangePasswordDTO.newPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Logout", description = "Logout the currently authenticated user.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        userServiceImpl.logout(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
