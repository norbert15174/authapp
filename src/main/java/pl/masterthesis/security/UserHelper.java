package pl.masterthesis.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.masterthesis.security.model.AuthenticatedUser;

import java.util.List;

@AllArgsConstructor
@Component
public class UserHelper {

    private final PasswordEncoder passwordEncoder;

    public List<UserDetails> getUsers() {
        return List.of(User.withUsername("first")
                        .password(passwordEncoder.encode("password"))
                        .roles("DEFAULT")
                        .build(),
                User.withUsername("second")
                        .password(passwordEncoder.encode("hhaus2bjs@jndna"))
                        .roles("DEFAULT")
                        .build());
    }

    public List<AuthenticatedUser> getCustomUsers() {
        return List.of(new AuthenticatedUser(getUsers().get(0), 1L), new AuthenticatedUser(getUsers().get(1), 2L));
    }

}
