package flashcards.server.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag implements DomainEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String name;

    // TODO: Do we really need this bidirectional?
    @ManyToOne
    @JoinColumn(nullable = false, name = "author_username")
    private User author;

//    @ManyToMany(mappedBy = "tags")
//    private Set<Flashcard> taggedFlashcards = new HashSet<>();

    public Tag() {

    }

    public Tag(String name, User author) {
        this.name = name;
        this.author = author;
    }


    public Tag(Integer id, String name, User author) {
        this(name, author);
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getAuthor() {
        return author;
    }

//    public Set<Flashcard> getTaggedFlashcards() {
//        return taggedFlashcards;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return getId() != null ? getId().equals(tag.getId()) : tag.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author.username=" + author.getUsername() +
                ", taggedFlashcardsCount=" + "taggedFlashcards.size()" +
                '}';
    }
}
