package pl.masterthesis.global.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.masterthesis.global.PerformanceConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AllArgsConstructor
@Component
public class RestClientService {

    private final RestTemplate baseRestTemplate;
    private final RestTemplate mainRestTemplate;
    private final PerformanceConfig performanceConfig;

    public ResponseEntity<String> noAuthTest() {
        if (performanceConfig.isNext()) {
            return ResponseEntity.ok(baseRestTemplate.getForObject(performanceConfig.getUrl() + "/noauth/performance", String.class));
        }

        return ResponseEntity.ok("Hello Performance Test!");
    }

    public ResponseEntity<String> basicTestPerformance() {

        var loginWithPassword = Base64.getEncoder().encodeToString("first:password".getBytes(StandardCharsets.UTF_8));
        var headers = new HttpHeaders();
        headers.add("Authorization", loginWithPassword);

        if (performanceConfig.isNext()) {
            return baseRestTemplate.exchange(performanceConfig.getUrl() + "/basic/auth/performance", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        }

        return ResponseEntity.ok("Hello Performance Test!");
    }

    public ResponseEntity<String> tokenTestPerformance() {
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2Vycy8xIiwiaWF0IjoxNzIwOTU2MjEzLCJpc3MiOiJtYXN0ZXItdGhlc2lzLWZhcm9uIiwianRpIjoiZjg5YjA2OTMtM2QyOS00MGMyLWIzZmUtZWZhZjMyNDAzNDM3Iiwicm9sZXMiOlsiUk9MRV9ERUZBVUxUIiwiUk9MRV9BRE1JTiJdLCJleHAiOjE3MjE4NTYyMTN9.K6cGJVa86s6UPQzma4PDkhUDtJ1IIaYzwbzhDhPkD00";
        var headers = new HttpHeaders();
        headers.add("Authorization", token);

        if (performanceConfig.isNext()) {
            return baseRestTemplate.exchange(performanceConfig.getUrl() + "/token/auth/performance", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        }

        return ResponseEntity.ok("Hello Performance Test!");
    }


    public ResponseEntity<String> apiKeyTestPerformance() {
        var apiKey = "njdah28dasjn12bdsadsa213dasda";
        var headers = new HttpHeaders();
        headers.add("X-API-KEY", apiKey);

        if (performanceConfig.isNext()) {
            return baseRestTemplate.exchange(performanceConfig.getUrl() + "/apikey/auth/performance", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        }

        return ResponseEntity.ok("Hello Performance Test!");
    }

    public ResponseEntity<String> certTestPerformance() {
        if (performanceConfig.isNext()) {
            return ResponseEntity.ok(mainRestTemplate.getForObject(performanceConfig.getUrl() + "/cert/auth/performance", String.class));
        }

        return ResponseEntity.ok("Hello Performance Test!");
    }
}
