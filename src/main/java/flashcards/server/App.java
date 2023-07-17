package flashcards.server;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "FlashcardsApp API", version = "0.0.1-SNAPSHOT"),
        servers = {@Server(url = "http://localhost:8080")}
)
@SecurityScheme(
        name = "BearerJWT",  // This is the name that will be used to reference this scheme in the components section.
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",     // This indicates that we're using HTTP Basic Authentication.
        bearerFormat = "JWT",
        description = "Bearer token"
)
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    }
}
