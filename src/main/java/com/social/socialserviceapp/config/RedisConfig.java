package com.social.socialserviceapp.config;

import io.lettuce.core.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(){
        return new RedisStandaloneConfiguration(host, port);
    }

    @Bean
    public ClientOptions clientOptions(){
        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .build();
    }

    @Bean
    public RedisConnectionFactory connectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration){
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
