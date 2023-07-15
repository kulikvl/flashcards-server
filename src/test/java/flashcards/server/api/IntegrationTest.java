package flashcards.server.api;

import flashcards.server.api.dto.UserDto;
import org.junit.jupiter.api.Assertions;
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


// We can do the same with postman or intellij idea http tests
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
        headers.set("Authorization", "Basic YWRtaW46MTIz");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<UserDto[]> response = restTemplate
                .exchange("http://localhost:" + port + "/users", HttpMethod.GET, entity, UserDto[].class);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("admin", response.getBody()[0].getUsername());
    }
}
