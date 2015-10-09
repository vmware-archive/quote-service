package org.springframework.nanotrader.quote;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class QuoteRepositoryPopulator implements
		ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	QuoteRepository quoteRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (quoteRepository != null && quoteRepository.count() == 0) {
			populate(quoteRepository);
		}
	}

	public void populate(QuoteRepository repository) {
		try {
			URI u = new ClassPathResource("quotes.json").getURI();
			byte[] jsonData = Files.readAllBytes(Paths.get(u));

			ObjectMapper objectMapper = new ObjectMapper();
			ArrayList<Quote> q = objectMapper.readValue(jsonData,
					new TypeReference<List<Quote>>() {
					});
			repository.save(q);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}