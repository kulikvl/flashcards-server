package flashcards.server.api;

import flashcards.server.business.AbstractCrudService;
import flashcards.server.business.EntityStateException;
import flashcards.server.domain.DomainEntity;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Function;


// TODO: May be used someday, may be not (only UserController can use it, but still requires a lot of override due to OpenAPI annotations)
public abstract class AbstractCrudController<E extends DomainEntity<ID>, DTO, ID> extends AbstractController<E, DTO, ID> {

    public AbstractCrudController(AbstractCrudService<E, ID> service,
                                  Function<E, DTO> toDtoConverter,
                                  Function<DTO, E> toEntityConverter) {
        super(service, toDtoConverter, toEntityConverter);
    }

    @PostMapping
//    @ResponseBody // when you annotate a class with @RestController, every method in that class is treated as if it is annotated with @ResponseBody, and the return value of those methods is written directly to the HTTP response body.
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "409", description = "an entity with the same ID already exists", content = @Content)
    })
    @SecurityRequirement(name = "basicAuth")
    public DTO create(@RequestBody DTO dto) {
        try {
            return toDtoConverter.apply(service.create(toEntityConverter.apply(dto)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public Collection<DTO> readAll() {
        return service.readAll().stream().map(toDtoConverter).toList();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    public DTO readOne(@PathVariable ID id) {
        try {
            return toDtoConverter.apply(service.readById(id).get());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basicAuth")
    public void update(@RequestBody DTO dto, @PathVariable ID id) {
        try {
            service.update(toEntityConverter.apply(dto));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basicAuth")
    public void delete(@PathVariable ID id) {
        service.deleteById(id);
    }
}

