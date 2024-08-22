package com.Jobseeker.Jobseeker.util;

import com.Jobseeker.Jobseeker.Config.JwtService;
import com.Jobseeker.Jobseeker.auth.AuthenticationRequest;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetAuthenticationJWT {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public GetAuthenticationJWT(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public AuthenticationRequest setupMockAuthentication(String email, String password, String expectedToken) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        User mockUser = new User();
        mockUser.setEmail(authenticationRequest.getEmail());
        mockUser.setPassword(authenticationRequest.getPassword());

        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(mockUser.getEmail(), mockUser.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        when(userRepository.findByEmail(authenticationRequest.getEmail()))
                .thenReturn(Optional.of(mockUser));

        when(jwtService.generateToken(any(User.class)))
                .thenReturn(expectedToken);
        return authenticationRequest;
    }
}