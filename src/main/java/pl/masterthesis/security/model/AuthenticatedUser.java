package pl.masterthesis.security.model;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;

@Getter
public class AuthenticatedUser extends User {

    @Serial
    private static final long serialVersionUID = 7140562305534691690L;

    private final Long id;

    public AuthenticatedUser(UserDetails userDetails, Long id) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.id = id;
    }
}
