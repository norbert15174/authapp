package pl.masterthesis.security.jwt.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.masterthesis.security.UserHelper;
import pl.masterthesis.security.jwt.JwtManageService;
import pl.masterthesis.security.jwt.helper.JwtHelper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static pl.masterthesis.security.jwt.helper.JwtHelper.TOKEN_HEADER;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtManageService jwtManageService;
    private final List<String> authList;
    private final UserHelper userHelper;

    public JwtAuthenticationFilter(JwtManageService jwtManageService, List<String> authList, UserHelper userHelper) {
        this.jwtManageService = jwtManageService;
        this.authList = authList;
        this.userHelper = userHelper;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return authList.stream()
                .anyMatch(authSkipPath -> authSkipPath.contains("**")
                        ? request.getServletPath().startsWith(authSkipPath.replace("**", ""))
                        : Objects.equals(request.getServletPath(), authSkipPath));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwtToken = request.getHeader(TOKEN_HEADER);

        try {
            verifyToken(jwtToken, request, response);
        } catch (JwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void verifyToken(String jwtToken, HttpServletRequest request, HttpServletResponse response) {
        if (Strings.isBlank(jwtToken)) {
            throw new JwtException("Jwt token not sent");
        }

        if (!jwtManageService.verify(jwtToken)) {
            throw new JwtException("Invalid token");
        }

        login(jwtToken);
    }

    private void login(String jwtToken) {
        var claims = jwtManageService.getClaims(jwtToken);
        var subject = JwtHelper.getIdFromSubject(claims);
        var authenticatedUser = userHelper.getCustomUsers()
                .stream()
                .filter(u -> u.getId().equals(subject))
                .findFirst()
                .orElseThrow(() -> new JwtException("User not exist"));
        var authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}