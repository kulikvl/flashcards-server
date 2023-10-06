package flashcards.server.api;

import flashcards.server.business.FlashcardService;
import flashcards.server.business.TagService;
import flashcards.server.business.UserService;
import flashcards.server.dao.jpa.FlashcardJpaRepository;
import flashcards.server.dao.jpa.TagJpaRepository;
import flashcards.server.dao.jpa.UserJpaRepository;
import flashcards.server.domain.Tag;
import flashcards.server.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
@Import({TagService.class, FlashcardService.class, UserService.class})
public class TagApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagService tagService;

    @MockBean
    private TagJpaRepository tagJpaRepository;

    @MockBean
    private FlashcardJpaRepository flashcardJpaRepository;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @Test
    @WithMockUser
    public void shouldCreateTag() throws Exception {
        User user = new User("eliska", "123");
        Mockito.when(userJpaRepository.findById("eliska")).thenReturn(Optional.of(user));

        Tag tag = new Tag(1, "Fav", user);
        Mockito.when(tagJpaRepository.findById(1)).thenReturn(Optional.of(tag));
        Mockito.when(tagJpaRepository.findAllByAuthorUsername("eliska")).thenReturn(List.of(tag));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/eliska/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists());

    }
}

