package pl.masterthesis.global.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.masterthesis.global.PerformanceConfig;
@AllArgsConstructor
@Component
public class RestClientService {

    private final RestTemplate baseRestTemplate;
    private final PerformanceConfig performanceConfig;

    public ResponseEntity<String> noAuthTest() {
        if(performanceConfig.isNext()){
            return ResponseEntity.ok(baseRestTemplate.getForObject(performanceConfig.getUrl() + "/noauth/performance", String.class));
        }

        return ResponseEntity.ok("Hello Performance Test!");
    }

}
