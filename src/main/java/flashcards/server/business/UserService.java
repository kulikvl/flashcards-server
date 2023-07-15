package flashcards.server.business;

import flashcards.server.dao.jpa.UserJpaRepository;
import flashcards.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractCrudService<User, String> {

    private final UserDetailsManager userDetailsManager;

    @Autowired
    public UserService(UserJpaRepository repository, UserDetailsManager userDetailsManager) {
        super(repository);
        this.userDetailsManager = userDetailsManager;
    }

    private UserDetails createUserDetails(String username, String password) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .roles("USER")
                .build();
    }

    @Override
    public User create(User entity) throws EntityStateException {
        if (userDetailsManager.userExists(entity.getId()))
            throw new EntityStateException("User " + entity.getId() + " already exists");

        userDetailsManager.createUser(createUserDetails(entity.getUsername(), entity.getPassword()));

        return readById(entity.getId()).orElseThrow(() -> new EntityStateException("It cannot happen, because UserDetailsManager and UserJpaRepository use the same table in the database"));
    }

    @Override
    public void update(User entity) throws EntityStateException {
        if (!userDetailsManager.userExists(entity.getId()))
            throw new EntityStateException("User " + entity + " does not exist");

        userDetailsManager.updateUser(createUserDetails(entity.getUsername(), entity.getPassword()));
    }

    @Override
    public void deleteById(String id) {
        userDetailsManager.deleteUser(id);
    }

//    public Optional<User> findUserByCreatedTagId(Integer tagId) {
//        return ((UserJpaRepository) repository).findUserByCreatedTagId(tagId);
//    }

    public void register(String username, String password) throws EntityStateException {
        if (userDetailsManager.userExists(username))
            throw new EntityStateException("User " + username + " already exists");

        UserDetails user = createUserDetails(username, password);

        userDetailsManager.createUser(user);

//        // print all users using reflection (for debugging and testing purposes)
//        Field usersMapField = ReflectionUtils.findField(InMemoryUserDetailsManager.class, "users");
//        ReflectionUtils.makeAccessible(usersMapField);
//        Map map = (Map) ReflectionUtils.getField(usersMapField, (InMemoryUserDetailsManager) userDetailsManager);
//        System.out.println(map);
    }

    public Collection<String> authenticate(String username, String password) throws BadCredentialsException{

        if (!userDetailsManager.userExists(username))
            throw new BadCredentialsException("User " + username + " does not exist");

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return userDetails.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
