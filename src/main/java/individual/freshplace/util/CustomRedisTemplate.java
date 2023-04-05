package individual.freshplace.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRedisTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    public void set(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Boolean existsKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(final String key) {
        redisTemplate.delete(key);
    }
}
