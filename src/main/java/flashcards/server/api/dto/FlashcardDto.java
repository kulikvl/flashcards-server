package flashcards.server.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public class FlashcardDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // This field will not be included in the request body
    private Long id;

    private String front;

    private String back;

    private String authorUsername;

    private Set<Integer> tagIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Set<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Integer> tagIds) {
        this.tagIds = tagIds;
    }
}
