package com.Jobseeker.Jobseeker.AuthenticateTests;

import com.Jobseeker.Jobseeker.Config.JwtService;
import com.Jobseeker.Jobseeker.auth.AuthenticationRequest;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.util.GetAuthenticationJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerAuthenticationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void shouldReturnJwtTokenWhenUserAuthenticateSuccessfully() {
        // given
        String expectedToken = "mocked-jwt-token";
        GetAuthenticationJWT getAuthenticationJWT = new GetAuthenticationJWT(authenticationManager, jwtService, userRepository);
        AuthenticationRequest authenticationRequest = getAuthenticationJWT.setupMockAuthentication("john.snow@gmail.com", "password", expectedToken);

        // when
        webTestClient
                .post()
                .uri("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authenticationRequest)
                .exchange()
                // then
                .expectStatus().isEqualTo(OK)
                .expectBody()
                .jsonPath("$.token").isEqualTo(expectedToken);
    }
}
