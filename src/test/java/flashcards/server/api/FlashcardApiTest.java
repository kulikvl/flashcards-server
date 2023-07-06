//package flashcards.server.api;
//
//import flashcards.server.business.AbstractCrudService;
//import flashcards.server.business.UserService;
//import flashcards.server.dao.jpa.UserJpaRepository;
//import flashcards.server.domain.User;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.jpa.repository.JpaRepository;
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
////  generally best to keep your tests focused on a single controller if possible
//@WebMvcTest({UserController.class, FlashcardController.class})
//@Import({UserService.class}) // import additional configuration classes if your controller relies on beans that aren't automatically loaded by @WebMvcTest
//public class FlashcardApiTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserJpaRepository userJpaRepository;
//
//    @MockBean
//    private UserDetailsManager userDetailsManager;
//
//    @Test
////    @WithMockUser(username = "a", roles = {"b"}, password = "c")
//    @WithMockUser // set up a mock user for your tests. This annotation sets up a SecurityContext for the test, which makes the system believe that there's a logged-in user.
//    public void shouldReturnUsers() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
////                .andExpect(status().isOk());
//
//        User user = new User("krevetka", "passw12");
//
//        Mockito.when(userJpaRepository.findById("krevetka")).thenReturn(Optional.of(user));
//
//        Mockito.when(userJpaRepository.findAll()).thenReturn(List.of(user));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
//                .andDo(print());
//
//    }
//}
