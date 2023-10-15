package com.social.socialserviceapp.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    @InjectMocks
    private RedisConfig redisConfig;

    @MockBean
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void redisStandaloneConfiguration(){
        try {
            redisConfig.redisStandaloneConfiguration();
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    void clientOptions(){
        redisConfig.clientOptions();
    }

    @Test
    void connectionFactory(){
        redisConfig.connectionFactory(new RedisStandaloneConfiguration());
    }

    @Test
    void redisTemplate(){
        redisConfig.redisTemplate(null);
    }
}