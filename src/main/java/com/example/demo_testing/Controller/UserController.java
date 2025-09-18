package com.example.demo_testing.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo_testing.Entity.User;
import com.example.demo_testing.Model.ApiResponse;
import com.example.demo_testing.Model.LoginRequest;
import com.example.demo_testing.Model.NameRequest;
import com.example.demo_testing.Model.RegisterRequest;
import com.example.demo_testing.Model.TokenResponse;
import com.example.demo_testing.Model.UserDTO;
import com.example.demo_testing.Service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Controller", description = "API for user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "api/auth/get/user")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile(user));
    }

    @GetMapping(path = "api/admin/get/user/all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllProfile());
    }

    @PatchMapping(path = "api/auth/update/user/name")
    public ResponseEntity<ApiResponse<UserDTO>> updateUsername(@AuthenticationPrincipal User user, @Valid @RequestBody NameRequest nameRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUsername(user, nameRequest));
    }
    
    @PostMapping(path = "api/user/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginRequest));
    }

    @PostMapping(path = "api/user/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequest));
    }
}
