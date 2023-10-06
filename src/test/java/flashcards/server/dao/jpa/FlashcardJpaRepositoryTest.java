package flashcards.server.dao.jpa;

import flashcards.server.domain.Flashcard;
import flashcards.server.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.HashSet;
import java.util.List;

@DataJpaTest(showSql = true)
public class FlashcardJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private FlashcardJpaRepository flashcardJpaRepository;

    @Test
    public void shouldFlashcardBeCreatedByUser() {
        User user = new User("eliska", "{noop}123");
        user = userJpaRepository.save(user);

        Flashcard flashcard = new Flashcard(null, "F1", "B1", new HashSet<>(), user);
        flashcard = flashcardJpaRepository.save(flashcard);

        List<Flashcard> flashcards = (List<Flashcard>) flashcardJpaRepository.findAllByAuthorUsername("eliska");

        Assertions.assertEquals(1, flashcards.size());
        Assertions.assertEquals("eliska", flashcards.get(0).getAuthor().getUsername());
    }

}
