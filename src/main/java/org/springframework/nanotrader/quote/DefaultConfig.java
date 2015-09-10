package org.springframework.nanotrader.quote;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import feign.Feign;
import feign.gson.GsonEncoder;

@Configuration
@EnableCaching
public class DefaultConfig {

	@Bean
	public QuoteRepository quoteRepository() {
		return createRepository("https://query.yahooapis.com/v1/public");
	}

	public QuoteRepository createRepository(String url) {
		return Feign.builder().encoder(new GsonEncoder())
				.decoder(new QuoteDecoder()).target(QuoteRepository.class, url);
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