package com.dkwasniak.slr_spot_backend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@RequiredArgsConstructor
public class EmailConnectionConfiguration {

    private final JavaMailSender javaMailSender;

    @Bean
    @ConditionalOnProperty(name = "mail.enabled", matchIfMissing = true)
    EmailSender emailService() {
        return new EmailService(javaMailSender);
    }

    @Bean
    @ConditionalOnProperty(name = "mail.enabled", havingValue = "false")
    EmailSender dummyEmailService() {
        return new DummyEmailService();
    }
}
