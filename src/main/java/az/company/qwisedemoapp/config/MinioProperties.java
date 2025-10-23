package az.company.qwisedemoapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url;
    private int port;
    private String accessKey;
    private String secretKey;
    private boolean secure;
    private Map<String, String> buckets;
}
