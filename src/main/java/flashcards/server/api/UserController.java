package flashcards.server.api;

import flashcards.server.api.dto.TagDto;
import flashcards.server.api.dto.UserDto;
import flashcards.server.business.EntityStateException;
import flashcards.server.domain.User;
import flashcards.server.business.UserService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/users")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Users")
public class UserController extends AbstractController<User, UserDto, String> {

    @Autowired
    public UserController(UserService service) {
        super(
                service,
                e -> {
                    UserDto dto = new UserDto();
                    dto.setUsername(e.getUsername());
                    dto.setPassword(e.getPassword());
                    return dto;
                },
                d -> new User(d.getUsername(), d.getPassword())
        );

    }

    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    @Operation(
            summary = "Create new user (only for admins)",
            security = {@SecurityRequirement(name = "basicAuth")},
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
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "OK")}
    )
    public Collection<UserDto> readAll() {
        return service.readAll().stream().map(toDtoConverter).toList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get the user (only for admins)",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "User not found")}
    )
    public UserDto readOne(@PathVariable String id) {
        try {
            return toDtoConverter.apply(service.readById(id).get());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    @Operation(
            summary = "Update the user (only for admins)",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "204", description = "Resource has been updated"), @ApiResponse(responseCode = "404", description = "User not found")}
    )
    public void update(@RequestBody UserDto dto) {
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete the user (only for admins)",
            security = {@SecurityRequirement(name = "basicAuth")}, // equivalent to separate annotation @SecurityRequirement(name = "basicAuth")
            responses = {@ApiResponse(responseCode = "204", description = "User has been deleted")} // equivalent to separate annotation @ResponseStatus(HttpStatus.NO_CONTENT)
    )
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }


    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            responses = {@ApiResponse(responseCode = "200", description = "User has been registered"), @ApiResponse(responseCode = "409", description = "User with the username already exists")}
    )
    public void register(@RequestBody UserDto dto) {
        try {
            ((UserService)service).register(dto.getUsername(), dto.getPassword());
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

//    @PutMapping("/test/{pv1}")
//    // @Operation is wrapper interface
//    @Operation(
//        tags = {"TAG1", "TAG2", "TAG3"}, // best use names like: Users (for a group of operations related to managing users in your app), Payments, ... best in CamelCase
////            operationId = "MY_OPERATION_ID",
//            summary = "THIS_IS_MY_SUMMARY",
//            description = "THIS_IS_MY_DESCRIPTION",
////            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "THIS_IS_REQUEST_BODY_DESC", content = @Content(schema = @Schema(implementation = TagDto.class)))
//            parameters = {@Parameter(name = "pv1", description = "MY_PV1_DESC", example = "3330")},
//            hidden = false,
//            externalDocs = @ExternalDocumentation(url = "http://google.com", description = "MY_EXTERNAL_DOCUM_DESC"),
//            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TagDto.class), mediaType = MediaType.APPLICATION_PDF_VALUE), description = "MY_200_DESC (FAKE CONTENT)")},
//            security = {@SecurityRequirement(name = "BearerJWT")}
//
//    )
//    public String testOpenAPIAnnotations(@PathVariable String pv1,
//                                         @RequestBody UserDto userDto,
//                                         @CookieValue(required = false) String COOKIE,
//                                         @RequestHeader Boolean header,
//                                         @RequestParam Boolean param) {
//        return "testing..." + pv1 + " {" + userDto.getUsername() + ", " + userDto.getPassword() + "} " + COOKIE + " " + header + " " + param;
//    }
//
//    @GetMapping("/greeting")
//    @Operation(
//            tags = {"TAG2", "TAG3"}
//    )
//    public String greeting() {
//        return "Hello, World!";
//    }
//
//    @GetMapping("/{userId}/secureGreeting")
//    @PreAuthorize("authentication.name == #userId")
//    public String secureGreeting(@PathVariable String userId) {
//        return "Hello, World! SECURED";
//    }

}
