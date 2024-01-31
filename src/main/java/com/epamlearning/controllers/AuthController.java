package com.epamlearning.controllers;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.RefreshRequestDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Auth Controller", description = "Controller for managing authentication")
public class AuthController implements BaseController {

    private final UserServiceImpl userServiceImpl;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(UserServiceImpl userServiceImpl, JWTUtil jwtUtil) {
        this.userServiceImpl = userServiceImpl;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Login", description = "Login with username and password. Token will be generated and returned. Token expiration time is 30 minutes.")
    @PostMapping("/login")
    public ResponseEntity<SessionDTO> login(@Validated @RequestBody UserAuthDTO userAuthDTO, HttpServletResponse response) {
        SessionDTO responseDTO = userServiceImpl.authenticate(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        Cookie cookie = new Cookie("access-token", responseDTO.accessToken());
        response.addCookie(cookie);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Refresh token", description = "Refresh token. New tokens (access, refresh) will be generated and returned. Token expiration time is 30 minutes.")
    @PostMapping("/refresh")
    public ResponseEntity<SessionDTO> refresh(@Validated @RequestBody RefreshRequestDTO refreshRequestDTO) {
        String refreshToken = refreshRequestDTO.refreshToken();
        String username = jwtUtil.extractUsername(refreshToken);
        if (jwtUtil.validateToken(refreshToken)) {
            return new ResponseEntity<>(userServiceImpl.generateTokens(username), HttpStatus.OK);
        } else {
            throw new NotAuthenticated("Invalid refresh token.");
        }
    }

    @Operation(summary = "Change password", description = "Change password for logged in user. Username and old password must be provided.")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        userServiceImpl.updatePassword(userChangePasswordDTO.username(), userChangePasswordDTO.oldPassword(), userChangePasswordDTO.newPassword());
        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }

    @Operation(summary = "Logout", description = "Logout the currently authenticated user.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        // Clear client-side cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return new ResponseEntity<>("Logout successful.", HttpStatus.OK);
    }


}
