package individual.freshplace.config;

import individual.freshplace.util.constant.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager redisCacheManager() {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration())
                .withInitialCacheConfigurations(customCacheConfiguration())
                .build();
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return redisCacheConfiguration;
    }

    private Map<String, RedisCacheConfiguration> customCacheConfiguration() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new LinkedHashMap<>();
        redisCacheConfigurationMap.put(Cache.GRADE,
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                        .entryTtl(Duration.ofSeconds(Cache.DEFAULT_EXPIRE_SECONDS)));

        redisCacheConfigurationMap.put(Cache.CATEGORY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(Cache.CATEGORY_EXPIRE)));

        redisCacheConfigurationMap.put(Cache.SUB_CATEGORY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(Cache.SUB_CATEGORY_EXPIRE)));

        redisCacheConfigurationMap.put(Cache.ITEMS_BY_CATEGORY,
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                        .entryTtl(Duration.ofDays(Cache.ITEMS_EXPIRE)));

        redisCacheConfigurationMap.put(Cache.ITEM,
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                        .entryTtl(Duration.ofDays(Cache.ITEM_EXPIRE)));

        return redisCacheConfigurationMap;
    }
}
