package com.example.demo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    private final RestClient restClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public DemoApplicationTests(RestClient.Builder restClientBuilder, @LocalServerPort int port) {
        restClient = restClientBuilder.baseUrl("http://localhost:" + port).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"with-constraint", "without-constraint"})
    void withConstraint(String variant) throws Exception {
        assertThatExceptionOfType(HttpClientErrorException.class).isThrownBy(() ->
            restClient.post()
                    .uri("/api/dummy/" + variant)
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                    })
                    .body("""
                    {
                      "propertyA": ""
                    }
                    """)
                    .retrieve().body(String.class)
        ).withMessageContaining(
            "Validation failed for object='requestData'. Error count: 1",
            "NotBlank.requestData.propertyA"
        );
    }

}
