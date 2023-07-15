package flashcards.server.dao.jpa;

import flashcards.server.domain.Flashcard;
import flashcards.server.domain.Tag;
import flashcards.server.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;

@DataJpaTest(showSql = true)
public class TagJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private FlashcardJpaRepository flashcardJpaRepository;

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @Test
    public void shouldTagBeAddedToFlashcard() {
        User user = new User("eliska", "{noop}123");
        user = userJpaRepository.save(user);

        Flashcard flashcard = new Flashcard(null, "F1", "B1", new HashSet<>(), user);
        flashcard = flashcardJpaRepository.save(flashcard);

        Tag tag = new Tag(null, "Fav", user);
        tag = tagJpaRepository.save(tag);

        flashcard.addTag(tag);
        flashcard = flashcardJpaRepository.save(flashcard);

        List<Tag> tags = (List<Tag>) tagJpaRepository.findAllByFlashcardId(flashcard.getId());

        Assertions.assertEquals(1, tags.size());
        Assertions.assertEquals(tag.getId(), tags.get(0).getId());
    }
}
