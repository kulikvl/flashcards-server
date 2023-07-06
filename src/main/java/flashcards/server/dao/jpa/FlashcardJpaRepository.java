package flashcards.server.dao.jpa;

import flashcards.server.domain.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FlashcardJpaRepository extends JpaRepository<Flashcard, Long> {

    Collection<Flashcard> findAllByAuthorUsername(String userId);

    @Query("SELECT f FROM Flashcard f JOIN f.tags t WHERE t.id IN :tagIds AND f.author.username = :userId")
    Collection<Flashcard> findAllByAuthorUsernameWithTags(@Param("userId") String userId, @Param("tagIds") Collection<Integer> tagIds);

}
