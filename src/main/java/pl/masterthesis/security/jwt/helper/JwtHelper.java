package pl.masterthesis.security.jwt.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtHelper {

    public static final String JWT_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String SUBJECT_PREFIX = "users/";

    public static String removeBearerText(String jwt) {
        return jwt.replace(JWT_PREFIX, "");
    }

    public static String generateSubject(Long id) {
        return String.format("%s%d", SUBJECT_PREFIX, id);
    }

    public static Long getIdFromSubject(Claims claims) {
        return Optional.ofNullable(claims.getSubject())
                .map(Object::toString)
                .map(value -> value.replace(SUBJECT_PREFIX, ""))
                .map(Long::valueOf)
                .orElseThrow(() -> new JwtException("Subject not exist in JWT token"));
    }


}
