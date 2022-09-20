package individual.freshplace.util;

import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

public class US_ASCII_StringSerializer extends StringRedisSerializer {

    @Override
    public byte[] serialize(String string) {
        return string.getBytes(StandardCharsets.US_ASCII);
    }

    @Override
    public String deserialize(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}
