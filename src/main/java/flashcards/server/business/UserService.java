package flashcards.server.business;

import flashcards.server.dao.jpa.UserJpaRepository;
import flashcards.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class UserService extends AbstractCrudService<User, String> {

    private final UserDetailsManager userDetailsManager;

    @Autowired
    public UserService(UserJpaRepository repository, UserDetailsManager userDetailsManager) {
        super(repository);
        this.userDetailsManager = userDetailsManager;
    }

    private UserDetails createUserDetails(String username, String password) {
        UserDetails user = org.springframework.security.core.userdetails.User
                .builder()
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .roles("USER")
                .build();
        return user;
    }

    @Override
    public User create(User entity) throws EntityStateException {
        if (userDetailsManager.userExists(entity.getId()))
            throw new EntityStateException("user " + entity.getId() + " already exists");

        userDetailsManager.createUser(createUserDetails(entity.getUsername(), entity.getPassword()));

        return readById(entity.getId()).get();
    }

    @Override
    public void update(User entity) throws EntityStateException {
        if (!userDetailsManager.userExists(entity.getId()))
            throw new EntityStateException("user " + entity + " does not exist");

        userDetailsManager.updateUser(createUserDetails(entity.getUsername(), entity.getPassword()));
    }

    @Override
    public void deleteById(String id) {
        userDetailsManager.deleteUser(id);
    }

    public void register(String username, String password) {
        if (userDetailsManager.userExists(username))
            throw new EntityStateException("user " + username + " already exists");

        UserDetails user = createUserDetails(username, password);

        userDetailsManager.createUser(user);

//        printUsersInMemoryMap((InMemoryUserDetailsManager) userDetailsManager);
    }

    public void printUsersInMemoryMap(InMemoryUserDetailsManager manager){
        Field usersMapField = ReflectionUtils.findField(InMemoryUserDetailsManager.class, "users");
        ReflectionUtils.makeAccessible(usersMapField);
        Map map = (Map) ReflectionUtils.getField(usersMapField, manager);
        System.out.println(map);
    }

}
