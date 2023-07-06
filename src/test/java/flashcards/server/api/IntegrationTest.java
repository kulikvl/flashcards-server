package flashcards.server.api;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // tells Spring Boot to start a real HTTP server listening on a random port
public class IntegrationTest {

    @LocalServerPort // inject the HTTP port that got allocated at runtime
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // convenience class that is useful for integration tests. It simplifies interaction with HTTP servers, and translates errors into HttpClientErrorExceptions or HttpServerErrorExceptions

    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    @Test
    @WithMockUser
    public void getUserShouldReturnUserDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic YWRtaW46MTIz");  // replace "token" with your actual token
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:" + port + "/users", HttpMethod.GET, entity, String.class);

//        ResponseEntity<String> response = restTemplate
//                .exchange("http://localhost:" + port + "/users/{id}", HttpMethod.GET, entity, String.class, 1);

//        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/users/{id}", String.class, 1);

        System.out.println("Response: " + response);
        logger.debug("DEBUG FROM LOGGER");
        logger.info("INFO FROM LOGGER");
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        assertThat(response.getBody()).contains("John Doe");
    }
}
