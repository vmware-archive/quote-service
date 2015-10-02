package org.springframework.nanotrader.quote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

	public static final String INDEX_SYMBOL = "^GSPC";

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	Symbols symbols;

	@RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
	public Quote findBySymbol(@PathVariable String symbol) {
		if (symbol == null || !symbols.exists(symbol)) {
			return null;
		}
		return quoteRepository.findOne(symbol);
	}

	private Quote getIndexInfo() {
		return quoteRepository.findOne(INDEX_SYMBOL);
	}

	@RequestMapping("/topGainers")
	public List<Quote> topGainers() {
		return quoteRepository
				.findAllByOrderByChangeDesc(new PageRequest(0, 3));
	}

	@RequestMapping("/topLosers")
	public List<Quote> topLosers() {
		return quoteRepository
				.findAllByOrderByChangeAsc(new PageRequest(0, 3));
	}

	@Cacheable(value = "index", key = "'marketSummary'")
	@RequestMapping("/marketSummary")
	public MarketSummary marketSummary() {
		return new MarketSummary(getIndexInfo());
	}

	@RequestMapping("/")
	public List<Quote> findAll() {
		Iterator<Quote> i = quoteRepository.findAll().iterator();
		List<Quote> l = new ArrayList<Quote>();
		while (i.hasNext()) {
			l.add(i.next());
		}
		return l;
	}

	@RequestMapping("/symbols")
	public Set<String> symbols() {
		return symbols.getSymbols();
	}

}