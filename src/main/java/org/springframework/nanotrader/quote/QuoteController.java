package org.springframework.nanotrader.quote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Cacheable(value = "index", key = "'count'")
	@RequestMapping("/count")
	public long countAllQuotes() {
		return symbols.getSymbols().size();
	}

	@Cacheable(value = "index", key = "'indexAverage'")
	@RequestMapping("/indexAverage")
	public float indexAverage() {
		return getIndexInfo().getPrice();
	}

	@Cacheable(value = "index", key = "'openAverage'")
	@RequestMapping("/openAverage")
	public float openAverage() {
		return getIndexInfo().getOpen();
	}

	@Cacheable(value = "index", key = "'volume'")
	@RequestMapping("/volume")
	public long volume() {
		return getIndexInfo().getVolume();
	}

	@Cacheable(value = "index", key = "'" + INDEX_SYMBOL + "'")
	public Quote getIndexInfo() {
		return getQuote(INDEX_SYMBOL);
	}

	private Quote getQuote(String symbol) {
		return quoteRepository
				.getQuote("select * from yahoo.finance.quotes where symbol = '"
						+ symbol + "'");
	}

	@Cacheable(value = "index", key = "'change'")
	@RequestMapping("/change")
	public float change() {
		return getIndexInfo().getChange();
	}

	@Cacheable(value = "index", key = "'topGainers'")
	@RequestMapping("/topGainers")
	public List<Quote> topGainers() {
		List<Quote> l = new ArrayList<Quote>(findAll());
		Collections.sort(l, new DescendingChangeComparator());
		return l.subList(0, 3);
	}

	@Cacheable(value = "index", key = "'topLosers'")
	@RequestMapping("/topLosers")
	public List<Quote> topLosers() {
		List<Quote> l = new ArrayList<Quote>(findAll());
		Collections.sort(l, new AscendingChangeComparator());
		return l.subList(0, 3);
	}

	@RequestMapping("/marketSummary")
	public Map<String, Object> marketSummary() {
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("tradeStockIndexAverage", indexAverage());
		ms.put("tradeStockIndexOpenAverage", openAverage());
		ms.put("tradeStockIndexVolume", volume());
		ms.put("cnt", countAllQuotes());
		ms.put("change", change());

		return ms;
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
}