package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM  Notification n LEFT JOIN Friend f ON f.id = n.friendId LEFT JOIN User u ON u.id = f.otherUserId WHERE u.id = ?1")
    public List<Notification> getNotificationsByUserId(Long userId);

}
