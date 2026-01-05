package com.AETHER.music.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Long> redisTemplate(
            RedisConnectionFactory factory
    ) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericToStringSerializer<Long> valueSerializer =
                new GenericToStringSerializer<>(Long.class);

        // Keys
        template.setKeySerializer(keySerializer);

        // Values (LIST, VALUE, HASH)
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        // Hash keys
        template.setHashKeySerializer(keySerializer);

        template.afterPropertiesSet();
        return template;
    }
}