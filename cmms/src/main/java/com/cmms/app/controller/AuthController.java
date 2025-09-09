package com.cmms.app.controller;

import com.cmms.app.dto.user.response.UserResponse;
import com.cmms.app.entity.User;
import com.cmms.app.mapper.UserMapper;
import com.cmms.app.repository.UserRepository;
import com.cmms.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test")
    public String testApi() {
        System.out.println("Calling test api------------");
        return "API is working!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String accessToken = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        String refreshToken = authService.createRefreshToken(
               userRepository.findByUsername(loginRequest.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"))
                        .getUserId()
        );
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse = userMapper.toUserResponse(user);
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, userResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String accessToken = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(new AuthResponse(accessToken, request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}

class LoginRequest {
    private String username;
    private String password;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class LogoutRequest {
    private String refreshToken;

    // Getters and setters
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}

class RefreshTokenRequest {
    private String refreshToken;

    // Getters and setters
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}

class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse userResponse;

    public LoginResponse(String accessToken, String refreshToken, UserResponse userResponse) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userResponse = userResponse;
    }

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public UserResponse getUserResponse() { return userResponse; }
}

class AuthResponse {
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}