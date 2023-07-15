package flashcards.server.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "flashcards")
public class Flashcard implements DomainEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String front;

    private String back;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "tagged_flashcards",
                joinColumns = @JoinColumn(name = "flashcard_id"),
                inverseJoinColumns = @JoinColumn(name = "tag_id") )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_username")
    private User author;

    public Flashcard() {

    }

    public Flashcard(Long id, String front, String back, Set<Tag> tags, User author) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.tags = tags;
        this.author = author;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public User getAuthor() {
        return author;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addTag(Tag tag) {
        tags.add(Objects.requireNonNull(tag));
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flashcard flashcard = (Flashcard) o;

        return getId() != null ? getId().equals(flashcard.getId()) : flashcard.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "id=" + id +
                ", front='" + front + '\'' +
                ", back='" + back + '\'' +
                ", author.username=" + author.getUsername() +
                ", tags=" + tags +
                '}';
    }
}
