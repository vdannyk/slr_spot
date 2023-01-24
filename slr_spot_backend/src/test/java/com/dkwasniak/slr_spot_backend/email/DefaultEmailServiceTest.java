package com.dkwasniak.slr_spot_backend.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DefaultEmailServiceTest {

    @InjectMocks
    private DefaultEmailService emailService;

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private TemplateEngine templateEngine;

    @Test
    public void sendEmail_shouldSendEmail_whenCorrectMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        emailService.sendEmail("me", "you", "test");

        verify(mailSender, times(1)).send((MimeMessage) any());
    }

    @Test
    public void sendVerificationEmail_shouldSendEmail_whenCorrectMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any())).thenReturn("test");

        emailService.sendVerificationEmail("me", "link");

        verify(mailSender, times(1)).send((MimeMessage) any());
    }

    @Test
    public void sendResetPasswordEmail_shouldSendEmail_whenCorrectMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any())).thenReturn("test");

        emailService.sendResetPasswordEmail("me", "link");

        verify(mailSender, times(1)).send((MimeMessage) any());
    }

}
