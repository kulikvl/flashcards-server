package flashcards.server.domain;

import javax.persistence.*;

@Entity
@Table(name = "tags")
public class Tag implements DomainEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_username")
    private User author;

    public Tag() {

    }

    public Tag(Integer id, String name, User author) {
        this.id = id;
        this.name = name;
        this.author = author;
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
                ", author.username=" + author.getUsername() + '}';
    }
}
