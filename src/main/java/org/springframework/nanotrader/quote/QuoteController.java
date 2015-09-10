package org.springframework.nanotrader.quote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

	@Autowired
	CacheManager manager;

	@Cacheable(value = "quotes", unless = "#result == null")
	@RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
	public Quote findBySymbol(@PathVariable String symbol) {
		if (symbol == null || !symbols.exists(symbol)) {
			return null;
		}
		return getQuote(symbol);
	}

	private long countAllQuotes() {
		return symbols.getSymbols().size();
	}

	private Quote getIndexInfo() {
		return getQuote(INDEX_SYMBOL);
	}

	private Quote getQuote(String symbol) {
		return quoteRepository
				.getQuote("select * from yahoo.finance.quotes where symbol = '"
						+ symbol + "'");
	}

	@RequestMapping("/topGainers")
	public List<Quote> topGainers() {
		List<Quote> l = new ArrayList<Quote>(findAll());
		Collections.sort(l, new DescendingChangeComparator());
		return l.subList(0, 3);
	}

	@RequestMapping("/topLosers")
	public List<Quote> topLosers() {
		List<Quote> l = new ArrayList<Quote>(findAll());
		Collections.sort(l, new AscendingChangeComparator());
		return l.subList(0, 3);
	}

	@Cacheable(value = "index", key = "'marketSummary'")
	@RequestMapping("/marketSummary")
	public MarketSummary marketSummary() {
		return new MarketSummary(getIndexInfo());
	}

	@RequestMapping("/")
	public List<Quote> findAll() {

		// check to see if we have all of our symbols in the cache
		Cache cache = manager.getCache("quotes");
		cache.evictExpiredElements();

		if (cache.getKeys().size() < countAllQuotes()) {
			// some are not in cache, clear things out and go get all of them.
			cache.removeAll();

			String s = "select * from yahoo.finance.quotes where symbol in "
					+ QuoteDecoder.formatSymbols(symbols.getSymbols());

			List<Quote> quotes = quoteRepository.getQuotes(s);

			// load these into the cache
			for (Quote q : quotes) {
				cache.put(new Element(q.getSymbol(), q));
			}
			return quotes;
		}

		// otherwise cache has everything, just return cached values
		List<Quote> l = new ArrayList<Quote>();

		@SuppressWarnings("rawtypes")
		List keys = cache.getKeys();
		for (Object key : keys) {
			Object o = cache.get(key).getObjectValue();
			if (o instanceof Quote) {
				l.add((Quote) o);
			}
		}
		return l;
	}

	@RequestMapping("/symbols")
	public Set<String> symbols() {
		return symbols.getSymbols();
	}

}