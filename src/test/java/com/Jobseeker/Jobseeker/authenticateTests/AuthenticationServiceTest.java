package com.Jobseeker.Jobseeker.authenticateTests;

import com.Jobseeker.Jobseeker.config.JwtService;
import com.Jobseeker.Jobseeker.auth.AuthenticationResponse;
import com.Jobseeker.Jobseeker.auth.AuthenticationService;
import com.Jobseeker.Jobseeker.auth.RegisterRequest;
import com.Jobseeker.Jobseeker.dataBase.repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private String jwtToken;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");
        jwtToken = "mocked-jwt-token";
    }

    @Test
    public void shouldRegisterUserAndReturnJwtToken() {
        // given
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);

        // when
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // then
        assertNotNull(response);
        assertEquals(jwtToken, response.token());
        verify(userRepository, times(1)).existsByEmail(registerRequest.email());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(registerRequest.password());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        // given
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(true);

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(registerRequest);
        });

        assertEquals("The email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(registerRequest.email());
        verify(userRepository, times(0)).save(any(User.class));
        verify(jwtService, times(0)).generateToken(any(User.class));
    }
}
