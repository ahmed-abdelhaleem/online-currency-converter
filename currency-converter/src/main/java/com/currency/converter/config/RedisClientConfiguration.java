package com.currency.converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.currency.converter.model.CurrencyCacheObject;

@Configuration
public class RedisClientConfiguration {

    @Bean
    public ReactiveRedisOperations<String, CurrencyCacheObject> reactiveRedisOperations(
            ReactiveRedisConnectionFactory factory) {

        Jackson2JsonRedisSerializer<CurrencyCacheObject> serializer = new Jackson2JsonRedisSerializer<>(CurrencyCacheObject.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, CurrencyCacheObject> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, CurrencyCacheObject> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}