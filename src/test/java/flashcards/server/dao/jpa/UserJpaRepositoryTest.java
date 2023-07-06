//package flashcards.server.dao.jpa;
//
//import flashcards.server.domain.Flashcard;
//import flashcards.server.domain.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import java.util.List;
//import java.util.Optional;
//
//@DataJpaTest
//public class UserJpaRepositoryTest {
//
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//
//    @Autowired
//    private FlashcardJpaRepository flashcardJpaRepository;
//
//    @Test
//    public void findAllUsers() {
////        User u1 = new User("username1");
////        userJpaRepository.save(u1);
////
////        List<User> users = userJpaRepository.findAll();
////
////        Assertions.assertEquals(1, users.size());
////        Assertions.assertEquals("username1", users.get(0).getUsername());
//    }
//
//    @Test
//    public void addFlashcardToUser() {
//
////        User user = new User("Eliska");
////        user = userJpaRepository.save(user);
////
////        Flashcard flashcard = new Flashcard("Co je bunka?", "Bunka je zakladni stavebni jednotka", user);
//////        user.createdFlashcards.add(flashcard);
////
//////        userJpaRepository.saveAndFlush()
////
////        flashcardJpaRepository.save(flashcard);
////
////        flashcardJpaRepository.flush();
////        userJpaRepository.flush();
////
////
////        System.out.println(userJpaRepository.findAll());
////        System.out.println(userJpaRepository.findById("Eliska").get().getCreatedFlashcards());
////
////        System.out.println(flashcardJpaRepository.findAll());
////        System.out.println(flashcardJpaRepository.findById(1L).get().getAuthor());
//
////        Optional<User> updatedUser = userJpaRepository.findById("Eliska");
////
////        Assertions.assertTrue(updatedUser.isPresent());
////
////        Assertions.assertNotNull(updatedUser.get().getCreatedFlashcards());
////
////        Optional<Flashcard> updatedFlashcard = flashcardJpaRepository.findById(1L);
////
////        Assertions.assertTrue(updatedFlashcard.isPresent());
////
////        Assertions.assertEquals(updatedFlashcard.get().getFront(), updatedUser.get().getCreatedFlashcards().get(0).getFront());
//    }
//
//}
