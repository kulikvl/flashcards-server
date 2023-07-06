//package flashcards.server.api;
//
//import flashcards.server.business.AbstractCrudService;
//import flashcards.server.business.UserService;
//import flashcards.server.dao.jpa.UserJpaRepository;
//import org.junit.jupiter.api.Test;
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
//import java.util.function.Function;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//@Import({UserService.class}) // import additional configuration classes if your controller relies on beans that aren't automatically loaded by @WebMvcTest
//public class GreetingTest {
//
//    //  is used to perform an HTTP GET request to the /greeting endpoint
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserJpaRepository userJpaRepository;
//
//    @MockBean
//    private UserDetailsManager userDetailsManager;
//
//
//    @Test
////    @WithMockUser(username = "a", roles = {"b"}, password = "c")
//    @WithMockUser // set up a mock user for your tests. This annotation sets up a SecurityContext for the test, which makes the system believe that there's a logged-in user.
//    public void greetingShouldReturnMessage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/users/greeting"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello, World!"));
//    }
//
//    @Test
////    @WithMockUser(username = "a", roles = {"b"}, password = "c")
//    @WithMockUser // set up a mock user for your tests. This annotation sets up a SecurityContext for the test, which makes the system believe that there's a logged-in user.
//    public void secureGreetingShouldReturnMessage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/users/lol/secureGreeting"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello, World! SECURED"));
//    }
//}
