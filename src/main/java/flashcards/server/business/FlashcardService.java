package flashcards.server.business;

import flashcards.server.dao.jpa.FlashcardJpaRepository;
import flashcards.server.domain.Flashcard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FlashcardService extends AbstractCrudService<Flashcard, Long> {

    @Autowired
    public FlashcardService(FlashcardJpaRepository repository) {
        super(repository);
    }

    public Collection<Flashcard> readAllByAuthor(String authorId) {
        return ((FlashcardJpaRepository) repository).findAllByAuthorUsername(authorId);
    }

    public Collection<Flashcard> readAllByAuthorWithTags(String authorId, Collection<Integer> tagIds) {
        return ((FlashcardJpaRepository) repository).findAllByAuthorUsernameWithTags(authorId, tagIds);
    }

}
