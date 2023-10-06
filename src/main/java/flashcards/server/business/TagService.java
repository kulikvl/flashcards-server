package flashcards.server.business;

import flashcards.server.dao.jpa.TagJpaRepository;
import flashcards.server.domain.Flashcard;
import flashcards.server.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

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

    @Transactional
    public void addTagToFlashcard(Integer tagId, Long flashcardId) throws EntityStateException {
        Optional<Tag> optionalTag = readById(tagId);
        Optional<Flashcard> optionalFlashcard = flashcardService.readById(flashcardId);

        Tag tag = optionalTag.orElseThrow(() -> new EntityStateException("Tag with id " + tagId + " does not exist")); // by default throws NoSuchElementException if the Optional is empty
        Flashcard flashcard = optionalFlashcard.orElseThrow(() -> new EntityStateException("Flashcard with id " + flashcardId + " does not exist"));

        flashcard.addTag(tag);

        flashcardService.update(flashcard);
    }

    @Transactional
    public void removeTagFromFlashcard(Integer tagId, Long flashcardId) throws EntityStateException {
        Optional<Tag> optionalTag = readById(tagId);
        Optional<Flashcard> optionalFlashcard = flashcardService.readById(flashcardId);

        Tag tag = optionalTag.orElseThrow(() -> new EntityStateException("Tag with id " + tagId + " does not exist")); // by default throws NoSuchElementException if the Optional is empty
        Flashcard flashcard = optionalFlashcard.orElseThrow(() -> new EntityStateException("Flashcard with id " + flashcardId + " does not exist"));

        flashcard.removeTag(tag);

        flashcardService.update(flashcard);
    }

    /*
        Spring Data Jpa's repository methods such as save(), delete(), ... are @Transactional.
        But if we are dealing with multiple operations (findById, removeTagFromFlashcard, deleteTag) that should be part of a single transaction (like here)
        We should use @Transactional at the service method level
     */
    @Override
    @Transactional
    public void deleteById(Integer id) throws EntityStateException {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new EntityStateException("Tag with id " + id + " does not exist"));

        for (Flashcard flashcard : flashcardService.readAllWithTags(Collections.singletonList(tag.getId()))) {
            removeTagFromFlashcard(tag.getId(), flashcard.getId());
        }

        repository.delete(tag);
    }

}
