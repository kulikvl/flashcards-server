package flashcards.server.dao.jpa;

import flashcards.server.domain.Tag;
import flashcards.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String> {

}

