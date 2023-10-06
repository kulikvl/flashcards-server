package flashcards.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import javax.annotation.PostConstruct;

@Configuration
public class UserInitializationConfig {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitializationConfig(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        if (!userDetailsManager.userExists("admin")) {
            UserDetails admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123"))
                    .roles("USER", "ADMIN")
                    .build();

            userDetailsManager.createUser(admin);
        }

        if (!userDetailsManager.userExists("test-user1")) {
            UserDetails testUser1 = User
                    .withUsername("test-user1")
                    .password("{noop}123")
                    .roles("USER")
                    .build();

            userDetailsManager.createUser(testUser1);
        }

    }

}
