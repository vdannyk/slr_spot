package com.dkwasniak.slr_spot_backend.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@NoArgsConstructor
@Data
public class PasswordResetToken {

    private static final int EXPIRATION_IN_SECONDS = 30 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    private LocalDateTime expiresAt;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiresAt = LocalDateTime.now().plusSeconds(EXPIRATION_IN_SECONDS);
    }
}
