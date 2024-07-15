package pl.masterthesis.global.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class RestConfig {

    private final SslBundles sslBundles;

    @Bean
    @Qualifier("baseRestTemplate")
    public RestTemplate baseRestTemplate(){
        return new RestTemplate();
    }

    @Qualifier("mainRestTemplate")
    @Bean
    RestTemplate mainRestTemplate() {
        return new RestTemplateBuilder().setSslBundle(sslBundles.getBundle("main")).build();
    }

}
