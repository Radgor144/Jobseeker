package com.Jobseeker.Jobseeker.jobseekerController;

import com.Jobseeker.Jobseeker.Config.JwtService;
import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.auth.RegisterRequest;
import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersEntity;
import com.Jobseeker.Jobseeker.dataBase.Repositories.ListOfOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserFavoriteOffersRepository;
import com.Jobseeker.Jobseeker.dataBase.Repositories.UserRepository;
import com.Jobseeker.Jobseeker.dataBase.User.Role;
import com.Jobseeker.Jobseeker.dataBase.User.User;
import com.Jobseeker.Jobseeker.util.JsonFileReaderToList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SearchJobOffersTest {

    public static final String URL = "/api/secure/offers?location=krakow&technology=java&experience=junior";
    private static final String OFFERS_DATA_RESOURCE_PATH = "src/test/java/resources/Example_of_searching_for_offers.json";

    @MockBean
    private ListOfOffersRepository listOfOffersRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserFavoriteOffersRepository userFavoriteOffersRepository;

    @Test
    void shouldAddOffersToDB() throws Exception {
        // given
        //        String bearerToken = "mocked.jwt.token";
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked.jwt.token");
        RegisterRequest registerRequest = new RegisterRequest("john", "snow", "john.snow@gmail.com", "password");
        var user = User.builder()
                .firstName(registerRequest.getFirstname())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        String bearerToken = jwtService.generateToken(user);

        // when

        System.out.println("XXXXXX");
        System.out.println(bearerToken);

        List<Offers> expectedOffersData = JsonFileReaderToList.readJson(objectMapper, OFFERS_DATA_RESOURCE_PATH, new TypeReference<>() {});

        stubFor(post(urlEqualTo(URL))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedOffersData)))
        );

        webTestClient
                .post()
                .uri(URL)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();


        List<OffersEntity> offersEntityList = listOfOffersRepository.findAll();
        System.out.println(offersEntityList);

        for (Offers expectedOffer : expectedOffersData) {
            boolean found = offersEntityList.stream()
                    .anyMatch(offerInDB -> offerInDB.equals(expectedOffer));
            assertThat(found).isTrue();
        }
    }
}
