package flashcards.server.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TagDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    private String name;

    private String authorUsername;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

}
