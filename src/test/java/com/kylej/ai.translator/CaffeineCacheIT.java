package com.kylej.ai.translator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

@SpringBootTest(properties = "cache-props.type=caffeine")
class CaffeineCacheIT {

  @Autowired
  private CacheManager cacheManager;

  @Test
  void verifyCaffeineCacheManager() {
    assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);
  }
}
