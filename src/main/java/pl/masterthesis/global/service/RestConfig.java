package pl.masterthesis.global.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestConfig {

    @Bean
    @Qualifier("baseRestTemplate")
    public RestTemplate baseRestTemplate(){
        return new RestTemplate();
    }

}
