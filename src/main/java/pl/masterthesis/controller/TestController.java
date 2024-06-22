package pl.masterthesis.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.masterthesis.model.LoginRequest;
import pl.masterthesis.security.jwt.JwtService;
import pl.masterthesis.security.jwt.model.TokenDTO;

@AllArgsConstructor
@RestController
public class TestController {

    private final JwtService jwtService;

    @GetMapping("/cert/auth")
    public String certTest() {
        return "Hello World";
    }

    @GetMapping("/basic/auth")
    public String basicTest() {
        return "Hello World";
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequest request) {
        return jwtService.login(request);
    }

}
