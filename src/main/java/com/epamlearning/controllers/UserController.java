package com.epamlearning.controllers;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.models.User;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController implements BaseController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public ResponseEntity<SessionDTO> login(UserAuthDTO userAuthDTO) {
        Long userId = userService.authenticate(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        String token = jwtUtil.generateToken(userAuthDTO.getUsername());
        SessionDTO sessionDTO = new SessionDTO(jwtUtil.getExpirationDateFromToken(token), jwtUtil.getIssuedAtDateFromToken(token), token);
        return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        System.out.println(userChangePasswordDTO.username());
        userService.updatePassword(userChangePasswordDTO.username(), userChangePasswordDTO.oldPassword(), userChangePasswordDTO.newPassword());
        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

}
