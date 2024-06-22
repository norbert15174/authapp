package pl.masterthesis.security.jwt;

import jakarta.servlet.http.HttpServletResponse;
import pl.masterthesis.model.LoginRequest;
import pl.masterthesis.security.jwt.model.TokenDTO;

public interface JwtService {
    TokenDTO login(LoginRequest request);
}
