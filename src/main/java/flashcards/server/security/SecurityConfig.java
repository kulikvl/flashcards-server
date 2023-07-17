package flashcards.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(JWTUtil jwtUtil, DataSource dataSource) {
        this.jwtUtil = jwtUtil;
        this.dataSource = dataSource;
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtil, userDetailsManager());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http    .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .cors().disable();

        http    .authorizeRequests()
                .antMatchers("/auth/*").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                .antMatchers("/users", "/users/*").hasRole("ADMIN")
                .antMatchers("/**").hasRole("USER")
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(
                            // Rejecting request as unauthorized when entry point is reached.
                            // If this point is reached it means that the current request requires authentication and no JWT token was found attached to the Authorization header of the current request.
                            (request, response, authException) ->
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                    );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
