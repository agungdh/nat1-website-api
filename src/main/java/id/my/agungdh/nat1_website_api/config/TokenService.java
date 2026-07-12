package id.my.agungdh.nat1_website_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String TOKEN_PREFIX = "auth:token:";

    private final StringRedisTemplate redisTemplate;

    @Value("${app.auth.expiration-ms}")
    private long expirationMs;

    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, username, expirationMs, TimeUnit.MILLISECONDS);
        return token;
    }

    public Optional<String> getUsername(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(TOKEN_PREFIX + token));
    }
}
