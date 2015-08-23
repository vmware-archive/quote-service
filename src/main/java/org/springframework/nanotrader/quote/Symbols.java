package org.springframework.nanotrader.quote;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Symbols {

	private Set<String> symbols;

	@Autowired
	QuoteController quoteController;

	public Set<String> getSymbols() {
		if (symbols != null) {
			return symbols;
		}

		try {
			URI u = new ClassPathResource("symbols.json").getURI();
			byte[] jsonData = Files.readAllBytes(Paths.get(u));

			ObjectMapper objectMapper = new ObjectMapper();
			ArrayList<Quote> quotes = objectMapper.readValue(jsonData,
					new TypeReference<List<Quote>>() {
					});

			symbols = new HashSet<String>();
			for (Quote quote : quotes) {
				symbols.add(quote.getSymbol());
			}

			return symbols;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean exists(String symbol) {
		if (symbol == null) {
			return false;
		}
		return getSymbols().contains(symbol);
	}

	public int count() {
		return getSymbols().size();
	}
}