package com.scoretracker.service.impl;

import com.scoretracker.dto.auth.UserProfileResponse;
import com.scoretracker.entity.Role;
import com.scoretracker.entity.User;
import com.scoretracker.enums.RoleName;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.UserMapper;
import com.scoretracker.repository.RoleRepository;
import com.scoretracker.repository.UserRepository;
import com.scoretracker.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserRoles(Long id, List<String> roleNames) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        Set<Role> newRoles = new HashSet<>();
        for (String roleNameStr : roleNames) {
            String formatted = roleNameStr.trim().toUpperCase();
            if (!formatted.startsWith("ROLE_")) {
                formatted = "ROLE_" + formatted;
            }
            try {
                RoleName roleName = RoleName.valueOf(formatted);
                Role role = roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName)));
                newRoles.add(role);
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (!newRoles.isEmpty()) {
            user.setRoles(newRoles);
        }

        User updated = userRepository.save(user);
        return userMapper.toProfileResponse(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }
}

