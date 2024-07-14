package pl.masterthesis.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.masterthesis.security.apikey.filter.ApiKeyAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
class ApiKeySecurityConfig {

    private static final String[] AUTH_WHITELIST = {
    };
    private final UserHelper userHelper;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring().requestMatchers("/**");
    }

    @Qualifier("securityFilterChain2")
    @Bean
    SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        http
         .cors(Customizer.withDefaults())
         .csrf(AbstractHttpConfigurer::disable)
         .sessionManagement(spec -> spec.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .exceptionHandling(handler -> new RestUnauthorizedHandler())
         .securityMatchers(requestMatcherConfigurer -> requestMatcherConfigurer.requestMatchers("/apikey/**"))
         .authorizeHttpRequests((requests) -> requests
                 .requestMatchers(AUTH_WHITELIST).permitAll()
                 .anyRequest().authenticated()
         ).logout(spec -> spec
                 .logoutUrl("/auth/logout")
                 .logoutSuccessUrl("/")
         )
         .addFilterBefore(apiKeyFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private ApiKeyAuthenticationFilter apiKeyFilter() {
        return new ApiKeyAuthenticationFilter(List.of(AUTH_WHITELIST), userHelper);
    }


}