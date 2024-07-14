package pl.masterthesis.security.model;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

@Getter
public class AuthenticatedUser extends User {

    @Serial
    private static final long serialVersionUID = 7140562305534691690L;

    private final Long id;
    private final List<ApiKeyModel> apiKeys;

    public AuthenticatedUser(UserDetails userDetails, Long id, List<ApiKeyModel> apiKeys) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.apiKeys = apiKeys;
        this.id = id;
    }

    public boolean hasApiKey(String value) {
        return Objects.nonNull(apiKeys) && apiKeys.stream().anyMatch(key -> key.value().equals(value));
    }

}
