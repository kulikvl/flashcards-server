package flashcards.server.api;

import flashcards.server.business.AbstractCrudService;
import flashcards.server.domain.DomainEntity;
import java.util.function.Function;

public abstract class AbstractController<E extends DomainEntity<ID>, DTO, ID> {

    protected AbstractCrudService<E, ID> service;
    protected Function<E, DTO> toDtoConverter;
    protected Function<DTO, E> toEntityConverter;

    public AbstractController(AbstractCrudService<E, ID> service,
                                  Function<E, DTO> toDtoConverter,
                                  Function<DTO, E> toEntityConverter) {
        this.service = service;
        this.toDtoConverter = toDtoConverter;
        this.toEntityConverter = toEntityConverter;
    }

}

