package com.social.socialserviceapp.util;

import com.social.socialserviceapp.exception.SocialAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final long timeExpired = 60 * 1000;

    public void setValue(String key, Integer value) throws SocialAppException{
        try {
            redisTemplate.opsForValue()
                    .set(key, String.valueOf(value));
            redisTemplate.expire(key, timeExpired, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
//            throw new SocialAppException("Error while saving to cache", e.getMessage());
        }
    }

    public void setValue(String key, String value) throws SocialAppException{
        try {
            redisTemplate.opsForValue()
                    .set(key, value);
            redisTemplate.expire(key, timeExpired, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
//            throw new SocialAppException("Error while saving to cache", e.getMessage());
        }
    }

    public Optional<String> getValue(String key) throws SocialAppException{
        try {
            Boolean b = redisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(b)) {
                return Optional.ofNullable(redisTemplate.opsForValue()
                        .get(key));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
//            throw new SocialAppException("Error while retrieving from the cache ", e.getMessage());
            throw new SocialAppException("Error while retrieving from the cache ");
        }
    }

    public void remove(String key){
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new SocialAppException("Error while removing from the cache");
        }
    }
}
