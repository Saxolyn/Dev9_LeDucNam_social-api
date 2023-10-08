package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.custom.ProfileCustom;
import com.social.socialserviceapp.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(value = "SELECT * FROM profiles p LEFT JOIN users u ON u.id = p.user_id WHERE u.username = :username",
            nativeQuery = true)
    Profile findProfileByUsername(@Param("username") String username);

    Profile findProfileByLastModifiedBy(String username);

    public List<Profile> getProfilesByRealNameContainsAndRealNameNot(String realName, String realNameUserLogin);

    @Query("SELECT new com.social.socialserviceapp.model.custom.ProfileCustom(u.id,u.username,p.realName,p.livePlace) FROM User u " +
            "LEFT JOIN Profile p ON u.username = p.createdBy " +
            "WHERE (u.username LIKE %?1%) AND u.username <> ?2")
    public List<ProfileCustom> findAllUsersByUsernameContain(String username, String usernameLogin);


}
