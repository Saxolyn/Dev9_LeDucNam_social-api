package com.social.socialserviceapp.util;

import com.social.socialserviceapp.exception.OtpCacheException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisUtilTest {

    @InjectMocks
    private RedisUtil redisUtil;
    @Mock
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations valueOperations;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void setValue(){
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations)
                .set(anyString(), anyString());
        redisUtil.setValue("aduchat", 3232);
    }

    @Test
    void setValue_whenKeyAndValueNull(){
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations)
                .set(anyString(), anyString());
        OtpCacheException exception = assertThrows(OtpCacheException.class, () -> redisUtil.setValue(null, null));
        assertTrue(exception.getMessage()
                .contains("Error while saving to cache."));
    }

    @Test
    void getValue(){
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisUtil.getValue("aduchat");
    }

    @Test
    void getValue_ifKeyNotFound(){
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        redisUtil.getValue("aduchat");
    }

    @Test
    void getValue_whenKeyNull(){
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        OtpCacheException exception = assertThrows(OtpCacheException.class, () -> redisUtil.getValue(null));
        assertTrue(exception.getMessage()
                .contains("Error while retrieving from the cache."));
    }

    @Test
    void remove(){
        when(redisTemplate.delete(anyString())).thenReturn(true);
        redisUtil.remove("aduchat");
    }

    @Test
    void remove_whenKeyNull(){
        when(redisTemplate.delete(anyString())).thenReturn(true);
        OtpCacheException exception = assertThrows(OtpCacheException.class, () -> redisUtil.remove(null));
        assertTrue(exception.getMessage()
                .contains("Error while removing from the cache."));
    }
}