package flashcards.server.business;

import flashcards.server.dao.jpa.FlashcardJpaRepository;
import flashcards.server.dao.jpa.TagJpaRepository;
import flashcards.server.dao.jpa.UserJpaRepository;
import flashcards.server.domain.Flashcard;
import flashcards.server.domain.Tag;
import flashcards.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.logging.Logger;

@Service
public class UserService extends AbstractCrudService<User, String> {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final UserDetailsManager userDetailsManager;
    private final FlashcardJpaRepository flashcardJpaRepository;
    private final TagJpaRepository tagJpaRepository;

    @Autowired
    public UserService(UserJpaRepository repository, UserDetailsManager userDetailsManager, FlashcardJpaRepository flashcardJpaRepository, TagJpaRepository tagJpaRepository) {
        super(repository);
        this.userDetailsManager = userDetailsManager;
        this.flashcardJpaRepository = flashcardJpaRepository;
        this.tagJpaRepository = tagJpaRepository;
    }

    private UserDetails createUserDetails(String username, String password) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .roles("USER")
                .build();
    }

    @Override
    public User create(User entity) throws EntityStateException {
        if (userDetailsManager.userExists(entity.getId()))
            throw new EntityStateException("User " + entity.getId() + " already exists");

        userDetailsManager.createUser(createUserDetails(entity.getUsername(), entity.getPassword()));

        return readById(entity.getId()).orElseThrow(() -> new EntityStateException("It cannot happen, because UserDetailsManager and UserJpaRepository use the same table in the database"));
    }

    @Override
    public void update(User entity) throws EntityStateException {
        if (!userDetailsManager.userExists(entity.getId()))
            throw new EntityStateException("User " + entity + " does not exist");

        userDetailsManager.updateUser(createUserDetails(entity.getUsername(), entity.getPassword()));
    }

//    @Override
//    @Transactional
//    public void deleteById(String id) {
//
//        Collection<Flashcard> flashcards = flashcardJpaRepository.findAllByAuthorUsername(id);
//
//        for (Flashcard flashcard : flashcards) {
//            flashcardJpaRepository.deleteById(flashcard.getId());
//        }
//
//        Collection<Tag> tags = tagJpaRepository.findAllByAuthorUsername(id);
//
//        for (Tag tag : tags) {
//            tagJpaRepository.deleteById(tag.getId());
//        }
//
//        flashcardJpaRepository.flush();
//        tagJpaRepository.flush();
//
//        userDetailsManager.deleteUser(id);
//    }

    // suppose all the logic is handled on the client side
    @Override
    public void deleteById(String id) {
        if (!userDetailsManager.userExists(id))
            throw new EntityStateException("User with id " + id + " does not exist");

        // if user has any flashcards or tags => abort
        if (!flashcardJpaRepository.findAllByAuthorUsername(id).isEmpty() || !tagJpaRepository.findAllByAuthorUsername(id).isEmpty()) {
            logger.info("User " + id + " has flashcards or tags, so can't be deleted");
            return;
        }

        userDetailsManager.deleteUser(id);
    }

}
