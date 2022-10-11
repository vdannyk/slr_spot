package com.dkwasniak.slr_spot_backend.email;

public interface EmailSender {
    void send(String to, String email);
}
