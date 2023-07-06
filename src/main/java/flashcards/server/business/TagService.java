package flashcards.server.business;

import flashcards.server.dao.jpa.FlashcardJpaRepository;
import flashcards.server.dao.jpa.TagJpaRepository;
import flashcards.server.domain.Flashcard;
import flashcards.server.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Optional;

@Service
public class TagService extends AbstractCrudService<Tag, Integer> {

    private final FlashcardService flashcardService;

    @Autowired
    public TagService(TagJpaRepository repository, FlashcardService flashcardService) {
        super(repository);
        this.flashcardService = flashcardService;
    }

    public Collection<Tag> readAllByAuthor(String authorId) {
        return ((TagJpaRepository) repository).findAllByAuthorUsername(authorId);
    }

    public Collection<Tag> readAllByFlashcard(Long flashcardId) {
        return ((TagJpaRepository) repository).findAllByFlashcardId(flashcardId);
    }

    public void addTagToFlashcard(Integer tagId, Long flashcardId) {
        Optional<Tag> optionalTag = readById(tagId);
        Optional<Flashcard> optionalFlashcard = flashcardService.readById(flashcardId);

        Tag tag = optionalTag.orElseThrow(); // throws NoSuchElementException if the Optional is empty
        Flashcard flashcard = optionalFlashcard.orElseThrow();
        flashcard.addTag(tag);

        flashcardService.update(flashcard);
    }

    public void removeTagFromFlashcard(Integer tagId, Long flashcardId) {
        Optional<Tag> optionalTag = readById(tagId);
        Optional<Flashcard> optionalFlashcard = flashcardService.readById(flashcardId);

        Tag tag = optionalTag.orElseThrow(); // throws NoSuchElementException if the Optional is empty
        Flashcard flashcard = optionalFlashcard.orElseThrow();

        flashcard.removeTag(tag);

        flashcardService.update(flashcard);
    }

}
