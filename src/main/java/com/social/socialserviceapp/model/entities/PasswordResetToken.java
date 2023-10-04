package com.social.socialserviceapp.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_name", unique = true)
    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_claimed")
    private Boolean claimed;
}
