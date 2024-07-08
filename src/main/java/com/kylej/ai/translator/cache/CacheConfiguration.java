package com.kylej.ai.translator.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.lettuce.core.RedisURI;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProps.class)
public class CacheConfiguration {

  public static final String TRANSLATION_CACHE = "translationCache";

  private final CacheProps cacheProps;

  @Bean
  @ConditionalOnProperty(prefix = "cache-props", name = "type", havingValue = "REDIS")
  public RedisConnectionFactory redisConnectionFactory() {
    RedisURI uri = RedisURI.create(cacheProps.getRedis().getHost());
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(uri.getHost());
    config.setPort(uri.getPort());
    return new LettuceConnectionFactory(config);
  }

  /**
   * Redis cache manager, present if cache-props.type is REDIS
   */
  @Bean
  @ConditionalOnProperty(prefix = "cache-props", name = "type", havingValue = "REDIS")
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues())
        .enableStatistics()
        .build();
  }

  /**
   * Caffeine cache manager, present if cache-props.type is CAFFEINE
   */
  @Bean
  @ConditionalOnProperty(prefix = "cache-props", name = "type", havingValue = "caffeine", matchIfMissing = true)
  public CaffeineCacheManager caffeineCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .maximumSize(cacheProps.getCaffeine().getMaximumSize())
        .expireAfterAccess(Duration.ofDays(cacheProps.getCaffeine().getExpireAfterAccessDays())));
    return new CaffeineCacheManager();
  }

}
