package com.sample.ehcache.ehcachedemo.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.*;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collections;
import java.util.EnumSet;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    CacheManager getCacheManager() {

        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        CacheConfigurationBuilder<Long, BigDecimal> configurationBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class, BigDecimal.class,
                                ResourcePoolsBuilder.heap(1000)
                                        .offheap(25, MemoryUnit.MB))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(3)));

        CacheEventListenerConfigurationBuilder asynchronousListener = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new CacheEventListener<Long, BigDecimal>() {
                                                   @Override
                                                   public void onEvent(CacheEvent<? extends Long, ? extends BigDecimal> event) {
                                                       System.out.println(event.getKey() + " " + event.getOldValue()+ " " + event.getNewValue());
                                                   }
                                               }
                        , EventType.CREATED, EventType.EXPIRED).unordered().asynchronous();

        //create caches we need
        cacheManager.createCache("squareCache",
                Eh107Configuration.fromEhcacheCacheConfiguration(configurationBuilder.withService(asynchronousListener)));

        return cacheManager;
    }

}