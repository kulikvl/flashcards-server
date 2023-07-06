package flashcards.server;

import flashcards.server.dao.jpa.FlashcardJpaRepository;
import flashcards.server.dao.jpa.UserJpaRepository;
import flashcards.server.domain.Flashcard;
import flashcards.server.domain.User;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO: REMOVE
@Component
public class Driver {

    public void start(ConfigurableApplicationContext context) {
//        UserJpaRepository userJpaRepository = context.getBean(UserJpaRepository.class);
//        FlashcardJpaRepository flashcardJpaRepository = context.getBean(FlashcardJpaRepository.class);
//
//        User user = new User("Eliska", "123");
//        Flashcard flashcard = new Flashcard("Co je bunka?", "Bunka je zakladni stavebni jednotka", user);
////        user.createdFlashcards.add(flashcard);
//
//        userJpaRepository.save(user);
//        flashcardJpaRepository.save(flashcard);
//
//        System.out.println(userJpaRepository.findAll());
//        System.out.println(userJpaRepository.findById("Eliska").get().getCreatedFlashcards());
//
//        System.out.println(flashcardJpaRepository.findAll());
//        System.out.println(flashcardJpaRepository.findById(1L).get().getAuthor());
    }

    public void simpleCreateScript(ConfigurableApplicationContext context) {
//        UserJpaRepository userJpaRepository = context.getBean(UserJpaRepository.class);
//        userJpaRepository.saveAll(List.of(new User("Eliska","{noop}1111"), new User("Vova","{noop}3333")));

    }
}
