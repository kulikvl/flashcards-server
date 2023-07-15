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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/users/{userId}/flashcards")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Flashcards")
@SecurityRequirement(name = "basicAuth")
public class FlashcardController extends AbstractController<Flashcard, FlashcardDto, Long> {

    @Autowired
    public FlashcardController(FlashcardService service, UserService userService) {
        super(
                service,
                e -> {
                    Set<TagDto> tagDtos = new HashSet<>();

                    if (e.getTags() != null) {
                        for (Tag tag : e.getTags()) {
                            tagDtos.add(new TagDto(tag.getId(), tag.getName(), tag.getAuthor().getUsername()));
                        }
                    }

                    return new FlashcardDto(e.getId(), e.getFront(), e.getBack(), tagDtos, e.getAuthor().getUsername());
                },
                d -> {
                    Set<Tag> tags = new HashSet<>();

                    if (d.getTags() != null) {
                        for (TagDto tagDto : d.getTags()) {
                            tags.add(new Tag(tagDto.getId(), tagDto.getName(), userService.readById(tagDto.getAuthorUsername()).get()));
                        }
                    }

                    return new Flashcard(d.getId(), d.getFront(), d.getBack(), tags, userService.readById(d.getAuthorUsername()).get());
                }
        );
    }

    @PostMapping
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Create new flashcard for the user",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)), @ApiResponse(responseCode = "409", description = "Username with the same ID already exists")}
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
            summary = "Get all the user's flashcards",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "OK")}
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
            summary = "Get the user's flashcard",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Flashcard not found")}
    )
    public FlashcardDto readOne(@PathVariable String userId, @PathVariable Long id) {
        try {
            return toDtoConverter.apply(service.readById(id).get());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    @PreAuthorize("authentication.name == #userId")
    @Operation(
            summary = "Update the user's flashcard",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "Flashcard has been updated"), @ApiResponse(responseCode = "404", description = "Flashcard not found")}
    )
    public void update(@PathVariable String userId, @RequestBody FlashcardDto dto) {
        dto.setAuthorUsername(userId);
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
    @Operation(
            summary = "Delete the user's flashcard",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {@ApiResponse(responseCode = "200", description = "Flashcard has been deleted")}
    )
    public void delete(@PathVariable String userId, @PathVariable Long id) {
        service.deleteById(id);
    }

}
