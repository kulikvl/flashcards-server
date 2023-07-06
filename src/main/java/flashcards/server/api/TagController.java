package flashcards.server.api;

import flashcards.server.api.dto.FlashcardDto;
import flashcards.server.api.dto.TagDto;
import flashcards.server.business.EntityStateException;
import flashcards.server.business.FlashcardService;
import flashcards.server.business.TagService;
import flashcards.server.business.UserService;
import flashcards.server.domain.Flashcard;
import flashcards.server.domain.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags")
@SecurityRequirement(name = "basicAuth")
public class TagController extends AbstractController<Tag, TagDto, Integer>{

    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    public TagController(TagService service, UserService userService) {
        super(
                service,
                e -> {
                    TagDto dto = new TagDto();
                    dto.setId(e.getId());
                    dto.setName(e.getName());
                    dto.setAuthorUsername(e.getAuthor().getUsername());
                    return dto;
                },
                d -> new Tag(d.getId(), d.getName(), userService.readById(d.getAuthorUsername()).get())
        );
    }

    @PostMapping("/tags")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Create new tag for the user"
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
            summary = "Get all the user's tags"
    )
    public Collection<TagDto> readAll(@PathVariable String userId) {
        return ((TagService)service).readAllByAuthor(userId).stream().map(toDtoConverter).toList();
    }

    @GetMapping("/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Get the user's tag"
    )
    public TagDto readOne(@PathVariable String userId, @PathVariable Integer id) {
        try {
            return toDtoConverter.apply(service.readById(id).get());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Update the user's tag"
    )
    public void update(@PathVariable String userId, @RequestBody TagDto dto, @PathVariable String id) {
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tags/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    @Operation(
            summary = "Delete the user's tag"
    )
    public void delete(@PathVariable String userId, @PathVariable Integer id) {
        service.deleteById(id);
    }

    @GetMapping("/flashcards/{flashcardId}/tags")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Get all the tags of the user's flashcard"
    )
    public Collection<TagDto> readAllByFlashcard(@PathVariable String userId, @PathVariable Long flashcardId) {
        return ((TagService)service).readAllByFlashcard(flashcardId).stream().map(toDtoConverter).toList();
    }

    @PostMapping("/flashcards/{flashcardId}/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Add the tag to the user's flashcard"
    )
    public void addTagToFlashcard(@PathVariable String userId, @PathVariable Integer id, @PathVariable Long flashcardId) {
        logger.debug("add tag " + id + " to flashcard " + flashcardId + " of user " + userId);
        System.out.println("AUUU");
        ((TagService) service).addTagToFlashcard(id, flashcardId);
    }

    @DeleteMapping("/flashcards/{flashcardId}/tags/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Remove the tag from the user's flashcard"
    )
    public void removeTagFromFlashcard(@PathVariable String userId, @PathVariable Integer id, @PathVariable Long flashcardId) {
        ((TagService) service).removeTagFromFlashcard(id, flashcardId);
    }

}
