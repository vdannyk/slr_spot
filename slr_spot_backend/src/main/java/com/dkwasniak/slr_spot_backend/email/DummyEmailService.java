package com.dkwasniak.slr_spot_backend.email;

public class DummyEmailService implements EmailService {

    public DummyEmailService() {
    }

    @Override
    public void sendEmail(String to, String title, String content) {
    }

    @Override
    public void sendVerificationEmail(String to, String link) {
    }

    @Override
    public void sendResetPasswordEmail(String to, String link) {
    }
}
