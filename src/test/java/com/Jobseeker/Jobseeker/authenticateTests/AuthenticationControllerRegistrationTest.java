package com.Jobseeker.Jobseeker.authenticateTests;

import com.Jobseeker.Jobseeker.config.JwtService;
import com.Jobseeker.Jobseeker.auth.AuthenticationResponse;
import com.Jobseeker.Jobseeker.auth.RegisterRequest;
import com.Jobseeker.Jobseeker.dataBase.repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerRegistrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @Test
    public void shouldReturnJwtTokenWhenUserRegistersSuccessfully() {
        // given
        String expectedToken = "mocked-jwt-token";
        RegisterRequest registerRequest = new RegisterRequest("john", "snow", "john.snow@gmail.com", "password");

        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);

        // when
        var response = webTestClient
                .post()
                .uri("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerRequest)
                .exchange()
                // then
                .expectStatus().isEqualTo(OK)
                .expectBody(AuthenticationResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals(response.token(), expectedToken);
    }
}
