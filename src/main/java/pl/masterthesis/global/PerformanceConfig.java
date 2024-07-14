package pl.masterthesis.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "performance")
public class PerformanceConfig {

    private String url;
    private boolean next;

}
