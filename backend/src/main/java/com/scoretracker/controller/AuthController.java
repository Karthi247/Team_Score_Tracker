package com.scoretracker.controller;

import com.scoretracker.dto.auth.*;
import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.security.CurrentUser;
import com.scoretracker.security.UserPrincipal;
import com.scoretracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<JwtResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        JwtResponse jwtResponse = authService.signup(signupRequest);
        return new ResponseEntity<>(ApiResponse.success("User registered successfully", jwtResponse), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        if (currentUser == null) {
            return new ResponseEntity<>(ApiResponse.error("User is not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        UserProfileResponse userProfile = authService.getCurrentUserProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.success(userProfile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(@CurrentUser UserPrincipal currentUser,
                                                                         @Valid @RequestBody UpdateProfileRequest request) {
        if (currentUser == null) {
            return new ResponseEntity<>(ApiResponse.error("User is not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        UserProfileResponse updatedProfile = authService.updateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updatedProfile));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@CurrentUser UserPrincipal currentUser,
                                                              @Valid @RequestBody ChangePasswordRequest request) {
        if (currentUser == null) {
            return new ResponseEntity<>(ApiResponse.error("User is not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        authService.changePassword(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", "OK"));
    }
}

