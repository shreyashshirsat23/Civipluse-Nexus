package com.civicpulse.nexus.config;

import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.*;
import java.time.Duration;

@Configuration
public class InfrastructureConfig {
    @Bean
    RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(60)).disableCachingNullValues()
                .serializeValuesWith(
                        org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory<String, com.civicpulse.nexus.events.GovernanceEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, com.civicpulse.nexus.events.GovernanceEvent> cf) {
        var f = new ConcurrentKafkaListenerContainerFactory<String, com.civicpulse.nexus.events.GovernanceEvent>();
        f.setConsumerFactory(cf);
        return f;
    }
}
