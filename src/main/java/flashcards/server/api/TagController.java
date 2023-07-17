package flashcards.server.api;

import flashcards.server.api.dto.TagDto;
import flashcards.server.business.EntityStateException;
import flashcards.server.business.TagService;
import flashcards.server.business.UserService;
import flashcards.server.domain.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collection;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/users/{userId}")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags")
@SecurityRequirement(name = "BearerJWT")
public class TagController extends AbstractController<Tag, TagDto, Integer>{

    @Autowired
    public TagController(TagService service, UserService userService) {
        super(
                service,
                e -> new TagDto(e.getId(), e.getName(), e.getAuthor().getUsername()),
                d -> new Tag(d.getId(), d.getName(), userService.readById(d.getAuthorUsername()).get())
        );
    }

    @PostMapping("/tags")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Create new tag for the user",
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)), @ApiResponse(responseCode = "409", description = "Tag with the same ID already exists")}
    )
    public TagDto create(@PathVariable String userId, @RequestBody TagDto dto) {
        dto.setAuthorUsername(userId);
        try {
            return toDtoConverter.apply(service.create(toEntityConverter.apply(dto)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/tags")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    @Operation(
            summary = "Get all the user's tags",
            responses = {@ApiResponse(responseCode = "200", description = "OK")}
    )
    public Collection<TagDto> readAll(@PathVariable String userId) {
        return ((TagService)service).readAllByAuthor(userId).stream().map(toDtoConverter).toList();
    }

    @GetMapping("/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Get the user's tag",
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Tag not found")}
    )
    public TagDto readOne(@PathVariable String userId, @PathVariable Integer id) {
        try {
            return toDtoConverter.apply(service.readById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Update the user's tag",
            responses = {@ApiResponse(responseCode = "200", description = "Tag has been updated"), @ApiResponse(responseCode = "404", description = "Tag not found")}
    )
    public void update(@PathVariable String userId, @RequestBody TagDto dto, @PathVariable String id) {
        dto.setAuthorUsername(userId);
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tags/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    @Operation(
            summary = "Delete the user's tag",
            responses = {@ApiResponse(responseCode = "200", description = "Tag has been deleted")}
    )
    public void delete(@PathVariable String userId, @PathVariable Integer id) {
        service.deleteById(id);
    }

    @GetMapping("/flashcards/{flashcardId}/tags")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Get all the tags of the user's flashcard",
            responses = {@ApiResponse(responseCode = "200", description = "OK")}
    )
    public Collection<TagDto> readAllByFlashcard(@PathVariable String userId, @PathVariable Long flashcardId) {
        return ((TagService)service).readAllByFlashcard(flashcardId).stream().map(toDtoConverter).toList();
    }

    @PostMapping("/flashcards/{flashcardId}/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Add the tag to the user's flashcard",
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Tag or flashcard not found")}
    )
    public void addTagToFlashcard(@PathVariable String userId, @PathVariable Integer id, @PathVariable Long flashcardId) {
        try {
            ((TagService) service).addTagToFlashcard(id, flashcardId);
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/flashcards/{flashcardId}/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Remove the tag from the user's flashcard",
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Tag or flashcard not found")}
    )
    public void removeTagFromFlashcard(@PathVariable String userId, @PathVariable Integer id, @PathVariable Long flashcardId) {
        try {
            ((TagService) service).removeTagFromFlashcard(id, flashcardId);
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
