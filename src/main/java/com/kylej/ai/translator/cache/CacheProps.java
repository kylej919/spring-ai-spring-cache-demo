package com.kylej.ai.translator.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cache-props")
public class CacheProps {

    private RedisCacheProps redis;
    private CaffeineCacheProps caffeine;
    private CacheType type;

    @Getter
    @Setter
    public static class RedisCacheProps {
        private String host;
    }

    @Getter
    @Setter
    public static class CaffeineCacheProps {
        private int maximumSize;
        private int expireAfterAccessDays;
    }
}
