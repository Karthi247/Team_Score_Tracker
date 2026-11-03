package com.scoretracker.service.impl;

import com.scoretracker.dto.auth.*;
import com.scoretracker.entity.Role;
import com.scoretracker.entity.User;
import com.scoretracker.enums.RoleName;
import com.scoretracker.exception.BadRequestException;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.UserMapper;
import com.scoretracker.repository.RoleRepository;
import com.scoretracker.repository.UserRepository;
import com.scoretracker.security.JwtTokenProvider;
import com.scoretracker.security.UserPrincipal;
import com.scoretracker.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider,
                           UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().trim().toLowerCase(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getFullName(),
                userPrincipal.getUsername(),
                userPrincipal.getMobileNumber(),
                roles
        );
    }

    @Override
    @Transactional
    public JwtResponse signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email address is already taken!");
        }

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm Password do not match!");
        }

        User user = new User(
                signupRequest.getFullName().trim(),
                email,
                signupRequest.getMobileNumber() != null ? signupRequest.getMobileNumber().trim() : "",
                passwordEncoder.encode(signupRequest.getPassword())
        );

        RoleName roleName = RoleName.ROLE_USER;
        if (signupRequest.getRole() != null) {
            try {
                String reqRole = signupRequest.getRole().toUpperCase().trim();
                if (!reqRole.startsWith("ROLE_")) {
                    reqRole = "ROLE_" + reqRole;
                }
                roleName = RoleName.valueOf(reqRole);
            } catch (IllegalArgumentException ex) {
                roleName = RoleName.ROLE_USER;
            }
        }

        Role userRole = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_USER)));

        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        User savedUser = userRepository.save(user);

        // Auto authenticate on signup
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, signupRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new JwtResponse(
                jwt,
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getMobileNumber(),
                List.of(userRole.getName().name())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUserProfile(UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        return userMapper.toProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setFullName(request.getFullName().trim());
        user.setMobileNumber(request.getMobileNumber().trim());

        User updatedUser = userRepository.save(user);
        return userMapper.toProfileResponse(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

