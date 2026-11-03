package com.scoretracker.service;

import com.scoretracker.dto.auth.LoginRequest;
import com.scoretracker.dto.auth.SignupRequest;
import com.scoretracker.dto.auth.JwtResponse;
import com.scoretracker.entity.Role;
import com.scoretracker.entity.User;
import com.scoretracker.enums.RoleName;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.mapper.UserMapper;
import com.scoretracker.repository.RoleRepository;
import com.scoretracker.repository.UserRepository;
import com.scoretracker.security.JwtTokenProvider;
import com.scoretracker.security.UserPrincipal;
import com.scoretracker.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role(1L, RoleName.ROLE_USER);
        sampleUser = new User("John Doe", "john@test.com", "1234567890", "encodedPass");
        sampleUser.setId(1L);
        sampleUser.setRoles(Collections.singleton(userRole));
    }

    @Test
    @DisplayName("Signup successfully with valid request")
    void testSignupSuccess() {
        SignupRequest request = new SignupRequest("John Doe", "john@test.com", "1234567890", "password123", "password123", "ROLE_USER");

        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("jwt.token.here");

        JwtResponse response = authService.signup(request);

        assertNotNull(response);
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("john@test.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Signup fails when email already exists")
    void testSignupDuplicateEmail() {
        SignupRequest request = new SignupRequest("John Doe", "john@test.com", "1234567890", "password123", "password123", "ROLE_USER");
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.signup(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Login successfully with valid credentials")
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("john@test.com", "password123");

        Authentication auth = mock(Authentication.class);
        UserPrincipal principal = UserPrincipal.create(sampleUser);
        when(auth.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("sample.jwt.token");

        JwtResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("sample.jwt.token", response.getToken());
        assertEquals(1L, response.getId());
    }
}
