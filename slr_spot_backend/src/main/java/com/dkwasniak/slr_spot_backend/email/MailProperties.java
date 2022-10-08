package com.dkwasniak.slr_spot_backend.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {

    private boolean enabled;
}
