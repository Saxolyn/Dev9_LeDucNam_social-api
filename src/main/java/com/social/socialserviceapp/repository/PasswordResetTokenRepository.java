package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByTokenAndClaimed(String token, boolean isClaimed);
    public void deleteByUserId(Long userId);
}
