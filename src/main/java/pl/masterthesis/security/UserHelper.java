package pl.masterthesis.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.masterthesis.security.model.ApiKeyModel;
import pl.masterthesis.security.model.AuthenticatedUser;

import java.util.List;

@AllArgsConstructor
@Component
public class UserHelper {

    private final PasswordEncoder passwordEncoder;

    public List<UserDetails> getUsers() {
        return List.of(User.withUsername("first")
                        .password(passwordEncoder.encode("password"))
                        .roles("DEFAULT", "ADMIN")
                        .build(),
                User.withUsername("second")
                        .password(passwordEncoder.encode("hhaus2bjs@jndna"))
                        .roles("DEFAULT")
                        .build(),
                User.withUsername("master-thesis-faron.pl")
                    .password("")
                    .roles("DEFAULT")
                    .build());
    }

    public List<AuthenticatedUser> getCustomUsers() {
        var apiKeyFirst = new ApiKeyModel("njdah28dasjn12bdsadsa213dasda", List.of("DEFAULT"));
        var apiKeySecond = new ApiKeyModel("asdh217kasdn2-dasbcasnj129nd2", List.of("ADMIN"));
        var apiKeyThird = new ApiKeyModel("asdh217kasdn2-dasbcasnj129nd2", List.of("DEFAULT"));

        return List.of(
                new AuthenticatedUser(getUsers().get(0), 1L, List.of(apiKeyFirst, apiKeySecond)),
                new AuthenticatedUser(getUsers().get(1), 2L, List.of(apiKeyThird))
        );
    }

}
