package flashcards.server.domain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements DomainEntity<String> {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Flashcard> createdFlashcards = new ArrayList<>();

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Flashcard> getCreatedFlashcards() {
        return Collections.unmodifiableList(createdFlashcards);
    }

    public void addFlashcard(Flashcard flashcard) {
        createdFlashcards.add(Objects.requireNonNull(flashcard));
    }

    // TODO: Move equals & hashCode to the parent class
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId() != null ? getId().equals(user.getId()) : user.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdFlashcardsCount= " + createdFlashcards.size() +
                '}';
    }
}
