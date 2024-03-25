package flashcards.server.api;

import flashcards.server.business.UserService;
import flashcards.server.dao.jpa.FlashcardJpaRepository;
import flashcards.server.dao.jpa.TagJpaRepository;
import flashcards.server.dao.jpa.UserJpaRepository;
import flashcards.server.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({UserService.class})
public class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @MockBean
    private FlashcardJpaRepository flashcardJpaRepository;

    @MockBean
    private TagJpaRepository tagJpaRepository;

    @Test
    @WithMockUser
    public void shouldReturnOneUser() throws Exception {
        User user = new User("eliska", "{noop}123");

        Mockito.when(userJpaRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].username").value("eliska"))
                .andExpect(jsonPath("$.[1].username").doesNotExist());

    }

}
