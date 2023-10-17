package flashcards.server.api;

import flashcards.server.api.dto.UserDto;
import flashcards.server.business.EntityStateException;
import flashcards.server.domain.User;
import flashcards.server.business.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collection;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/users")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Users")
@SecurityRequirement(name = "BearerJWT")
public class UserController extends AbstractController<User, UserDto, String> {

    @Autowired
    public UserController(UserService service) {
        super(
                service,
                e -> new UserDto(e.getUsername(), e.getPassword()),
                d -> new User(d.getUsername(), d.getPassword())
        );
    }

    @PostMapping
    @Operation(
            summary = "Create new user (only for admins)",
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)), @ApiResponse(responseCode = "409", description = "User with the same ID (username) already exists")}
    )
    public UserDto create(@RequestBody UserDto dto) {
        try {
            return toDtoConverter.apply(service.create(toEntityConverter.apply(dto)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all users (only for admins)",
            responses = {@ApiResponse(responseCode = "200", description = "OK")}
    )
    public Collection<UserDto> readAll() {
        return service.readAll().stream().map(toDtoConverter).toList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get the user (only for admins)",
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "User not found")}
    )
    public UserDto readOne(@PathVariable String id) {
        try {
            return toDtoConverter.apply(service.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update the user (only for admins)",
            responses = {@ApiResponse(responseCode = "200", description = "User has been updated"), @ApiResponse(responseCode = "404", description = "User not found")}
    )
    public void update(@PathVariable String id, @RequestBody UserDto dto) {
        dto.setUsername(id);
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete the user (only for admins)",
            responses = {@ApiResponse(responseCode = "200", description = "User has been deleted"), @ApiResponse(responseCode = "404", description = "User not found")}
    )
    public void delete(@PathVariable String id) {
        try {
            service.deleteById(id);
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
