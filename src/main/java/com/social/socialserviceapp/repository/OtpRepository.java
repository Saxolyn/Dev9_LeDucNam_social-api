package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.exception.SocialAppException;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository {

    default void put(String key, Integer value) throws SocialAppException{
        try {


        } catch (Exception e) {
            throw new SocialAppException("Error while saving cache", e.getMessage());
        }
    }
}
