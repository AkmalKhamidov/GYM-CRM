package com.epamlearning.controllers;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/login")
    public ResponseEntity<SessionDTO> login(UserAuthDTO userAuthDTO) {
        userService.authenticate(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        String token = jwtUtil.generateToken(userAuthDTO.getUsername());
        SessionDTO sessionDTO = new SessionDTO(jwtUtil.getExpirationDateFromToken(token), jwtUtil.getIssuedAtDateFromToken(token), token);
        return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
    }

    @Operation(summary = "Change password", description = "Change password for logged in user. Username and old password must be provided.")
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        userService.updatePassword(userChangePasswordDTO.username(), userChangePasswordDTO.oldPassword(), userChangePasswordDTO.newPassword());
        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }

}
