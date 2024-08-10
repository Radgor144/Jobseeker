package com.Jobseeker.Jobseeker.AuthenticateTests;

import com.Jobseeker.Jobseeker.auth.AuthenticationController;
import com.Jobseeker.Jobseeker.auth.AuthenticationResponse;
import com.Jobseeker.Jobseeker.auth.AuthenticationService;
import com.Jobseeker.Jobseeker.auth.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AuthenticationControllerRegistrationTest {

    @InjectMocks
    private AuthenticationController authController;

    @Mock
    private AuthenticationService authService;

    @Test
    public void shouldReturnJwtTokenWhenUserRegistersSuccessfully() {
        // given
        RegisterRequest request = new RegisterRequest("firstname", "lastname", "email@gmail.com", "password123");
        String expectedToken = "mocked-jwt-token";
        AuthenticationResponse expectedResponse = new AuthenticationResponse(expectedToken);

        when(authService.register(request)).thenReturn(expectedResponse);

        // when
        ResponseEntity<AuthenticationResponse> result = authController.register(request);

        // then
        log.info("Expected Token: " + expectedToken);
        log.info("Response Token: " + result.getBody().getToken());
        assertEquals(expectedResponse.getToken(), result.getBody().getToken());
    }
}
