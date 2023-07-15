package flashcards.server.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TagDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    private String name;

    private String authorUsername;

    public TagDto() {

    }

    public TagDto(Integer id, String name, String authorUsername) {
        this.id = id;
        this.name = name;
        this.authorUsername = authorUsername;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDto tag = (TagDto) o;

        return getId() != null ? getId().equals(tag.getId()) : tag.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

}
