package flashcards.server.dao.jpa;

import flashcards.server.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, Integer> {

    Collection<Tag> findAllByAuthorUsername(String userId);

    @Query("SELECT f.tags FROM Flashcard f WHERE f.id = :flashcardId")
    Collection<Tag> findAllByFlashcardId(@Param("flashcardId") Long flashcardId);

}
