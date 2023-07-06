package flashcards.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Authorities implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private User username;

    @Id
    private String authority;
}
