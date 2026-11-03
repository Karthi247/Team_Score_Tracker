package com.scoretracker.service;

import com.scoretracker.dto.auth.*;
import com.scoretracker.security.UserPrincipal;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse signup(SignupRequest signupRequest);
    UserProfileResponse getCurrentUserProfile(UserPrincipal currentUser);
    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    void changePassword(Long userId, ChangePasswordRequest request);
}

