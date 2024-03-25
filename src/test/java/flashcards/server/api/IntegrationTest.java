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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser
    public void getUserShouldReturnUserDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJpc3MiOiJzZXJ2ZXIvZmxhc2hjYXJkcy9rdWxpa3ZsIiwiaWF0IjoxNjg5NTQxNzM1LCJ1c2VybmFtZSI6ImFkbWluIn0.VQNvZwf-7PpaoBEbG0Ifp5-Ot4fmV9N7265BpSCvENI");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<UserDto[]> response = restTemplate
                .exchange("http://localhost:" + port + "/users", HttpMethod.GET, entity, UserDto[].class);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("admin", response.getBody()[0].getUsername());
    }
}
