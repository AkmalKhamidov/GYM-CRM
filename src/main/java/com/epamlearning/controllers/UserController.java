package com.epamlearning.controllers;

import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.dtos.user.UserChangePasswordDTO;
import com.epamlearning.models.User;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<Long> login(UserAuthDTO userAuthDTO) {
        Long userId = userService.authenticate(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        System.out.println(userChangePasswordDTO.getUsername());
        userService.updatePassword(userChangePasswordDTO.getUsername(), userChangePasswordDTO.getOldPassword(), userChangePasswordDTO.getNewPassword());
        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

}
