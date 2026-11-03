package com.scoretracker.service;

import com.scoretracker.dto.auth.UserProfileResponse;

import java.util.List;

public interface UserService {
    List<UserProfileResponse> getAllUsers();
    UserProfileResponse getUserById(Long id);
    UserProfileResponse updateUserRoles(Long id, List<String> roleNames);
    void deleteUser(Long id);
}

