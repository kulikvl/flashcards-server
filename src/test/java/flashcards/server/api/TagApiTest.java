//package flashcards.server.api;
//
//import flashcards.server.business.AbstractCrudService;
//import flashcards.server.business.FlashcardService;
//import flashcards.server.business.TagService;
//import flashcards.server.business.UserService;
//import flashcards.server.dao.jpa.FlashcardJpaRepository;
//import flashcards.server.dao.jpa.TagJpaRepository;
//import flashcards.server.dao.jpa.UserJpaRepository;
//import flashcards.server.domain.Flashcard;
//import flashcards.server.domain.Tag;
//import flashcards.server.domain.User;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.http.MediaType;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(TagController.class)
//@Import({TagService.class, FlashcardService.class, UserService.class}) // import additional configuration classes if your controller relies on beans that aren't automatically loaded by @WebMvcTest
//public class TagApiTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TagService tagService;
//
//    @MockBean
//    private TagJpaRepository tagJpaRepository;
//
//    @MockBean
//    private FlashcardJpaRepository flashcardJpaRepository;
//
//    @MockBean
//    private UserJpaRepository userJpaRepository;
//
//    @MockBean
//    private UserDetailsManager userDetailsManager;
//
//    @Test
////    @WithMockUser(username = "admin", password = "123", roles = {"USER", "ADMIN"})
//    @WithMockUser
//    public void shouldAddTagToFlashcardOfUser() throws Exception {
//        User user = new User("admin", "123");
//        Mockito.when(userJpaRepository.findById("admin")).thenReturn(Optional.of(user));
//
//        Tag tag = new Tag(1, "Fav", user);
//        Mockito.when(tagJpaRepository.findById(1)).thenReturn(Optional.of(tag));
//
//        Flashcard flashcard = new Flashcard(1L, "F", "B", user, null);
//        Mockito.when(flashcardJpaRepository.findById(1L)).thenReturn(Optional.of(flashcard));
//
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/users/admin/tags"))
//                .andDo(print());
//
//
////        String jsonData = "{\"username\":\"testuser\",\"password\":\"testpassword\"}";
////
////        mockMvc.perform(
////                MockMvcRequestBuilders
////                        .post("/users/krevetka/flashcards/1/tags")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(jsonData)
////        )
////                ;
//
//
////        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string("Test User"));
//
////        mockMvc.perform(MockMvcRequestBuilders.get("/users/krevetka/flashcards/1/tags"))
////                .andDo(print());
//
//
//    }
//}
//
