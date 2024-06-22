package pl.masterthesis.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@AllArgsConstructor
@Configuration
public class SecurityBasicConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserHelper userHelper;

    @Bean
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(handler -> new RestUnauthorizedHandler())
                .sessionManagement(withDefaults())
                .securityMatchers(matcher -> matcher.requestMatchers("/basic/**"))
                .authorizeHttpRequests(spec -> spec.anyRequest().authenticated())
                .userDetailsService(customUserDetailsService)
                .httpBasic(withDefaults());

        return http.build();
    }

}
