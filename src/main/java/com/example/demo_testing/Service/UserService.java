package com.example.demo_testing.Service;

import java.util.List;

import com.example.demo_testing.Entity.User;
import com.example.demo_testing.Model.ApiResponse;
import com.example.demo_testing.Model.LoginRequest;
import com.example.demo_testing.Model.NameRequest;
import com.example.demo_testing.Model.RegisterRequest;
import com.example.demo_testing.Model.TokenResponse;
import com.example.demo_testing.Model.UserDTO;

public interface UserService {
    ApiResponse<UserDTO> getProfile(User user);
    ApiResponse<List<UserDTO>> getAllProfile();
    ApiResponse<UserDTO> updateUsername(User user, NameRequest nameRequest);
    ApiResponse<TokenResponse> login(LoginRequest loginRequest);
    ApiResponse<String> register(RegisterRequest registerRequest);
}
