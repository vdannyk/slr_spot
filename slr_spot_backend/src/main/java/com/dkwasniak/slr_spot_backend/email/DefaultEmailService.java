package com.dkwasniak.slr_spot_backend.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Slf4j
public class DefaultEmailService implements EmailService {

    private static final String VERIFICATION_EMAIL_TEMPLATE = "verification-email";
    private static final String RESET_PASSWORD__EMAIL_TEMPLATE = "reset-password-email";
    private static final String VERIFICATION_EMAIL_TITLE = "Confirm your account";
    private static final String RESET_PASSWORD_EMAIL_TITLE = "Reset your password";
    private static final String EMAIL_FROM = "slrspot.welcome@gmail.com";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    @Override
    public void sendEmail(String to, String title, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setFrom(EMAIL_FROM);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    @Override
    public void sendVerificationEmail(String to, String link) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();
        context.setVariable("link", link);
        String content = loadEmailTemplate(context, VERIFICATION_EMAIL_TEMPLATE);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(VERIFICATION_EMAIL_TITLE);
            helper.setFrom(EMAIL_FROM);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send verification email", e);
            throw new IllegalStateException("Failed to send verification email");
        }
    }

    @Async
    @Override
    public void sendResetPasswordEmail(String to, String link) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();
        context.setVariable("link", link);
        String content = loadEmailTemplate(context, RESET_PASSWORD__EMAIL_TEMPLATE);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(RESET_PASSWORD_EMAIL_TITLE);
            helper.setFrom(EMAIL_FROM);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send reset password email", e);
            throw new IllegalStateException("Failed to send reset password email");
        }
    }

    private String loadEmailTemplate(Context context, String name) {
        return templateEngine.process(name, context);
    }
}
