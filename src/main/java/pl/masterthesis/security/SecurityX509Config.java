package pl.masterthesis.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;

@EnableWebSecurity
@AllArgsConstructor
@Configuration
class SecurityX509Config {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    SecurityFilterChain authClientCertFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handler -> new RestUnauthorizedHandler())
                .sessionManagement(Customizer.withDefaults())
                .securityMatchers(matcher -> matcher.requestMatchers("/cert/**"))
                .authorizeHttpRequests(spec -> spec.anyRequest().authenticated())
                .x509(Customizer.withDefaults())
                .userDetailsService(customUserDetailsService)
                .build();
    }

    @Bean
    X509PrincipalExtractor customX509PrincipalExtractor() {
        return obj -> obj.getSubjectX500Principal().getName();
    }

}
