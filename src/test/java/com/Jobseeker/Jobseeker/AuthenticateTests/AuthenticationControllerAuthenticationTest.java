package com.Jobseeker.Jobseeker.AuthenticateTests;

import com.Jobseeker.Jobseeker.Config.JwtService;
import com.Jobseeker.Jobseeker.auth.AuthenticationRequest;
import com.Jobseeker.Jobseeker.auth.AuthenticationResponse;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerAuthenticationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void shouldReturnJwtTokenWhenUserAuthenticateSuccessfully() {
        // given
        String expectedToken = "mocked-jwt-token";
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john.snow@gmail.com", "password");

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

        // when
        var response = webTestClient
                .post()
                .uri("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authenticationRequest)
                .exchange()
                // then
                .expectStatus().isEqualTo(OK)
                .expectBody(AuthenticationResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals(response.getToken(), expectedToken);
    }
}