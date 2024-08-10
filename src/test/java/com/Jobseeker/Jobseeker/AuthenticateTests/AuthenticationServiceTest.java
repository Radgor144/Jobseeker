package com.Jobseeker.Jobseeker.AuthenticateTests;

import com.Jobseeker.Jobseeker.auth.AuthenticationResponse;
import com.Jobseeker.Jobseeker.auth.AuthenticationService;
import com.Jobseeker.Jobseeker.auth.RegisterRequest;
import com.Jobseeker.Jobseeker.Config.JwtService;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.User.Role;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private User user;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        jwtToken = "mocked-jwt-token";
    }

    @Test
    public void shouldRegisterUserAndReturnJwtToken() {
        // given
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // then
        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(registerRequest.getPassword());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        // given
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(registerRequest);
        });

        assertEquals("The email already exists", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(jwtService, times(0)).generateToken(any(User.class));
    }
}
