package pl.masterthesis.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.masterthesis.security.jwt.helper.JwtHelper;
import pl.masterthesis.security.model.AuthenticatedUser;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class JwtManageServiceImpl implements JwtManageService {

    private final JwtProperties jwtProperties;

    @Override
    public String createAccessToken(AuthenticatedUser user, String subject) {

        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .issuer(jwtProperties.getIssuer())
                .id(UUID.randomUUID().toString())
                .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .expiration(getExpirationDate(jwtProperties.getAccessTokenExpirationTime()))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String createRefreshToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .issuer(jwtProperties.getIssuer())
                .id(UUID.randomUUID().toString())
                .expiration(getExpirationDate(jwtProperties.getRefreshTokenExpirationTime()))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean isExpired(String token) {
        try {
            var secret = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(JwtHelper.removeBearerText(token));
            return false;
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    @Override
    public boolean verify(String token) {
        if (Objects.isNull(token) || token.isBlank()) {
            return false;
        }

        try {
            var secret = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(JwtHelper.removeBearerText(token));
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        var secret = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(JwtHelper.removeBearerText(token))
                .getPayload();
    }

    private Key getSigningKey() {
        var keyBytes = this.jwtProperties.getKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date getExpirationDate(Integer expirationTime) {
        var issuedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        var expiration = issuedAt.plus(expirationTime, ChronoUnit.MILLIS);
        return Date.from(expiration);
    }

}
