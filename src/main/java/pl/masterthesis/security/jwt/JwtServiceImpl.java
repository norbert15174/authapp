package pl.masterthesis.security.jwt;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.masterthesis.model.LoginRequest;
import pl.masterthesis.security.UserHelper;
import pl.masterthesis.security.jwt.helper.JwtHelper;
import pl.masterthesis.security.jwt.model.TokenDTO;
import pl.masterthesis.security.model.AuthenticatedUser;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
class JwtServiceImpl implements JwtService {


    private final JwtManageService jwtManageService;
    private final UserHelper userHelper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenDTO login(LoginRequest request) {
        var userOpt = userHelper.getCustomUsers().stream()
                .filter(u -> u.getUsername().equals(request.username()))
                .findFirst();

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Cannot authenticate user - not found");
        }

        var user = userOpt.get();
        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            return generateToken(userOpt.get());
        }

        throw new BadCredentialsException("Invalid username or password");
    }

    private TokenDTO generateToken(AuthenticatedUser user) {
        var subject = JwtHelper.generateSubject(user.getId());
        var accessToken = jwtManageService.createAccessToken(user, subject);
        var refreshToken = jwtManageService.createRefreshToken(subject);
        var clientId = UUID.randomUUID()
                .toString()
                .replace("-", "");

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .clientId(clientId)
                .build();
    }

}
