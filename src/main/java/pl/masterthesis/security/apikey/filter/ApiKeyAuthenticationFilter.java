package pl.masterthesis.security.apikey.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.masterthesis.security.UserHelper;
import pl.masterthesis.security.apikey.exception.ApiKeyException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String API_KEY_HEADER = "X-API-KEY";
    private final List<String> authList;
    private final UserHelper userHelper;

    public ApiKeyAuthenticationFilter(List<String> authList, UserHelper userHelper) {
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
        var apiKey = request.getHeader(API_KEY_HEADER);

        try {
            verifyApiKey(apiKey);
        } catch (ApiKeyException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void verifyApiKey(String apiKey) {
        if (Objects.isNull(apiKey)) {
            throw new ApiKeyException("Api Key not sent");
        }

        var authenticatedUser = userHelper.getCustomUsers()
                .stream()
                .filter(u -> u.hasApiKey(apiKey))
                .findFirst()
                .orElseThrow(() -> new ApiKeyException("User not exist"));

        var roles = authenticatedUser.getApiKeys()
                .stream()
                .filter(key -> key.value().equals(apiKey))
                .findFirst()
                .get()
                .roles();

        var authorities = authenticatedUser.getAuthorities().stream()
                .filter(authority -> roles.contains(authority.getAuthority()))
                .collect(Collectors.toSet());

        var authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
