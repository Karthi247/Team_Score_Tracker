package com.scoretracker.controller;

import com.scoretracker.dto.auth.UserProfileResponse;
import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllUsers() {
        List<UserProfileResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserById(@PathVariable Long id) {
        UserProfileResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserRoles(@PathVariable Long id, @RequestBody List<String> roleNames) {
        UserProfileResponse updated = userService.updateUserRoles(id, roleNames);
        return ResponseEntity.ok(ApiResponse.success("User roles updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", "DELETED"));
    }
}

