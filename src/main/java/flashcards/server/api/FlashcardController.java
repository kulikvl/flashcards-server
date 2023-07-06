package flashcards.server.api;

import flashcards.server.api.dto.FlashcardDto;
import flashcards.server.api.dto.UserDto;
import flashcards.server.business.EntityStateException;
import flashcards.server.business.FlashcardService;
import flashcards.server.business.TagService;
import flashcards.server.business.UserService;
import flashcards.server.domain.Flashcard;
import flashcards.server.domain.Tag;
import flashcards.server.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/users/{userId}/flashcards")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Flashcards")
@SecurityRequirement(name = "basicAuth")
public class FlashcardController extends AbstractController<Flashcard, FlashcardDto, Long> {

    @Autowired
    public FlashcardController(FlashcardService service, UserService userService, TagService tagService) {
        super(
                service,
                e -> {
                    FlashcardDto dto = new FlashcardDto();
                    dto.setId(e.getId());
                    dto.setFront(e.getFront());
                    dto.setBack(e.getBack());
                    dto.setAuthorUsername(e.getAuthor().getUsername());
                    Set<Integer> tagIds = e.getTags() == null ? null : e.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
                    dto.setTagIds(tagIds);
                    return dto;
                },
                d -> {
                    Set<Tag> tags = d.getTagIds() == null ? null : d.getTagIds().stream().map(i -> tagService.readById(i).get()).collect(Collectors.toSet());
                    return new Flashcard(d.getId(), d.getFront(), d.getBack(), userService.readById(d.getAuthorUsername()).get(), tags);
                }
        );
    }

    @PostMapping
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Create new flashcard for the user"
    )
    public FlashcardDto create(@PathVariable String userId, @RequestBody FlashcardDto dto) {
        dto.setAuthorUsername(userId);
        try {
            return toDtoConverter.apply(service.create(toEntityConverter.apply(dto)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    @Operation(
            summary = "Get all the user's flashcards"
    )
    public Collection<FlashcardDto> readAll(@PathVariable String userId, @RequestParam(name = "tags", required = false) Collection<Integer> tagIds) {
        if (tagIds == null) {
            return ((FlashcardService) service).readAllByAuthor(userId).stream().map(toDtoConverter).toList();
        } else {
            return ((FlashcardService) service).readAllByAuthorWithTags(userId, tagIds).stream().map(toDtoConverter).toList();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Get the user's flashcard"
    )
    public FlashcardDto readOne(@PathVariable String userId, @PathVariable Long id) {
        try {
            return toDtoConverter.apply(service.readById(id).get());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Update the user's flashcard"
    )
    public void update(@PathVariable String userId, @RequestBody FlashcardDto dto, @PathVariable Long id) {
        dto.setAuthorUsername(userId);
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    @Operation(
            summary = "Delete the user's flashcard"
    )
    public void delete(@PathVariable String userId, @PathVariable Long id) {
        service.deleteById(id);
    }

}
