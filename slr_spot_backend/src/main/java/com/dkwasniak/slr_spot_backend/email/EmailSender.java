package com.dkwasniak.slr_spot_backend.email;

public interface EmailSender {
    void sendEmail(String to, String title, String content);
    void sendVerificationEmail(String to, String link);
    void sendResetPasswordEmail(String to, String link);
}
