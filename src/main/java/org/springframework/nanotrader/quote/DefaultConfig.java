package org.springframework.nanotrader.quote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import feign.gson.GsonEncoder;

@Configuration
public class DefaultConfig {

	@Bean
	public QuoteRepository quoteRepository() {
		return createRepository("https://query.yahooapis.com/v1/public");
	}

	public QuoteRepository createRepository(String url) {
		QuoteRepository qr = Feign.builder().encoder(new GsonEncoder())
				.decoder(new QuoteDecoder()).target(QuoteRepository.class, url);

		return qr;
	}
}