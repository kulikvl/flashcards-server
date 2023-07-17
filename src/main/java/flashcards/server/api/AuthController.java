package flashcards.server.api;

import flashcards.server.api.dto.UserDetailsDto;
import flashcards.server.api.dto.UserDto;
import flashcards.server.security.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Auth")
public class AuthController {

    private final UserDetailsManager userDetailsManager;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthController(UserDetailsManager userDetailsManager, JWTUtil jwtUtil, AuthenticationManager authManager) {
        this.userDetailsManager = userDetailsManager;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            responses = {@ApiResponse(responseCode = "200", description = "User has been registered"), @ApiResponse(responseCode = "409", description = "User with the same username already exists")}
    )
    public Map<String, Object> registerHandler(@RequestBody UserDto userDto) {

        if (userDetailsManager.userExists(userDto.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT);

        UserDetails user = org.springframework.security.core.userdetails.User
                .builder()
                .username(userDto.getUsername())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDto.getPassword()))
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);

        String token = jwtUtil.generateToken(user.getUsername());

        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate the user",
            responses = {@ApiResponse(responseCode = "200", description = "User has been successfully authenticated and his roles are returned"), @ApiResponse(responseCode = "403", description = "Authentication failed")}
    )
    public Map<String, Object> loginHandler(@RequestBody UserDto userDto) {

        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(userDto.getUsername());

            return Collections.singletonMap("jwt-token", token);

        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/info")
    @Operation(
            summary = "Get info about currently authenticated user (me)",
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "401", description = "Authentication failed")}
    )
    public UserDetailsDto getUserDetails(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Collection<String> authorityNames = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        return new UserDetailsDto(userDetails.getUsername(), userDetails.getPassword(), authorityNames);
    }

}
