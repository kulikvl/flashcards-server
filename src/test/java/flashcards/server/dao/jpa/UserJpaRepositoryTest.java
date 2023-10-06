package flashcards.server.dao.jpa;

import flashcards.server.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

@DataJpaTest(showSql = true)
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    public void shouldBeOneUser() {
        User u1 = new User("eliska", "{noop}123");
        userJpaRepository.save(u1);

        List<User> users = userJpaRepository.findAll();

        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("eliska", users.get(0).getUsername());
    }
}
