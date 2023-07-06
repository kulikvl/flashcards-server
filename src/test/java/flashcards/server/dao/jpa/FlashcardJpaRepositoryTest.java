//package flashcards.server.dao.jpa;
//
//import flashcards.server.domain.Flashcard;
//import flashcards.server.domain.Tag;
//import flashcards.server.domain.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Commit;
//
//import javax.transaction.Transactional;
//
//@DataJpaTest(showSql = true)
//public class FlashcardJpaRepositoryTest {
//
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//
//    @Autowired
//    private FlashcardJpaRepository flashcardJpaRepository;
//
//    @Autowired
//    private TagJpaRepository tagJpaRepository;
//
//    @Test
//    public void test() {
//        User user = new User("Eliska", "111");
//        user = userJpaRepository.save(user);
//
//        Flashcard flashcard = new Flashcard("A", "B", user);
//        user.addFlashcard(flashcard);
//
//        flashcard = flashcardJpaRepository.save(flashcard);
//
//        Assertions.assertEquals("A", user.getCreatedFlashcards().get(0).getFront());
//        Assertions.assertEquals("Eliska", flashcard.getAuthor().getUsername());
//
//        Tag tag = new Tag("Fav", user);
//        tag = tagJpaRepository.save(tag);
//
//        flashcard.addTag(tag);
////        tag.getTaggedFlashcards().add(flashcard);
//
//        flashcard = flashcardJpaRepository.save(flashcard);
//        tag = tagJpaRepository.save(tag);
//
//        Assertions.assertEquals(1, flashcard.getTags().size());
//        Assertions.assertEquals(1, tag.getTaggedFlashcards().size());
//
//    }
//}
