package org.springframework.nanotrader.quote;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class DefaultConfig {

    @Bean
    public QuoteRepository quoteRepository() {
        return createRepository("https://api.iextrading.com/1.0");
    }

    public QuoteRepository createRepository(String url) {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new  QuoteDecoder())
                .logger(new Logger.JavaLogger())
                .logLevel(Logger.Level.FULL)
                .target(QuoteRepository.class, url);
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(cacheFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean cacheFactory() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }
}