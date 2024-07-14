package pl.masterthesis.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.masterthesis.global.service.RestClientService;
import pl.masterthesis.model.LoginRequest;
import pl.masterthesis.security.jwt.JwtService;
import pl.masterthesis.security.jwt.model.TokenDTO;

@AllArgsConstructor
@RestController
public class TestController {

    private final JwtService jwtService;
    private final RestClientService restClientService;

    @GetMapping("/cert/auth")
    public String certTest() {
        return "Hello World";
    }

    @GetMapping("/basic/auth")
    public String basicTest() {
        return "Hello World";
    }

    @GetMapping("/token/auth")
    public String tokenTest() {
        return "Hello World";
    }

    @GetMapping("/apikey/auth")
    public String apiKeyTest() {
        return "Hello World";
    }

    @GetMapping("/noauth/performance")
    public ResponseEntity<String> noAuthTestPerformance() {
        return restClientService.noAuthTest();
    }

    @GetMapping("/cert/auth/performance")
    public String certTestPerformance() {
        return "Hello World";
    }

    @GetMapping("/basic/auth/performance")
    public String basicTestPerformance() {
        return "Hello World";
    }

    @GetMapping("/token/auth/performance")
    public String tokenTestPerformance() {
        return "Hello World";
    }

    @GetMapping("/apikey/auth/performance")
    public String apiKeyTestPerformance() {
        return "Hello World";
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequest request) {
        return jwtService.login(request);
    }

}
