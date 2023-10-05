package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(value = "SELECT * FROM profiles p LEFT JOIN users u ON u.id = p.user_id WHERE u.username = :username",
            nativeQuery = true)
    Profile findProfileByUsername(@Param("username") String username);

    Profile findProfileByLastModifiedBy(String username);
}
