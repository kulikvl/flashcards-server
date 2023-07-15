package flashcards.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();

//        // permit all requests
//        http    .authorizeRequests()
//                .anyRequest()
//                .permitAll();

        http    .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/register", "/users/authenticate").permitAll()
                .antMatchers("/users", "/users/*").hasRole("ADMIN")
                .antMatchers("/**").hasRole("USER")
                .and()
                .httpBasic();

        return http.build();
    }

    // TODO: Remove when database is persistent
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager conf = new JdbcUserDetailsManager(dataSource);

        UserDetails admin = User.withDefaultPasswordEncoder() // default is {bcrypt}...
            .username("admin")
            .password("123")
            .roles("ADMIN", "USER")
            .build();

        UserDetails user1 = User.withDefaultPasswordEncoder()
            .username("user1")
            .password("123")
            .roles("USER")
            .build();

        UserDetails user2 = User.withDefaultPasswordEncoder()
            .username("user2")
            .password("123")
            .roles("USER")
            .build();

        UserDetails user3 = User.withUsername("user3")
            .password("{noop}123")
            .roles("USER", "ADMIN")
            .build();

        conf.createUser(admin);
        conf.createUser(user1);
        conf.createUser(user2);
        conf.createUser(user3);

        return conf;
    }

//    // in-memory users storage
//    @Bean
//    public UserDetailsManager userDetailsService() {
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("123")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("123")
//                .roles("ADMIN")
//                .build();
//
////        System.out.println("BEAN:userDetailsService(): " + user.getPassword());
//        // echo -n "admin:123" | base64
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

}
