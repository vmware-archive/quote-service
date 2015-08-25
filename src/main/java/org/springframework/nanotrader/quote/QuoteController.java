package org.springframework.nanotrader.quote;

import java.util.ArrayList;
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

	@Cacheable("quotes")
	@RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
	public Quote findBySymbol(@PathVariable String symbol) {
		if (symbol == null || !symbols.exists(symbol)) {
			return null;
		}

		return quoteRepository
				.getQuote("select * from yahoo.finance.quotes where symbol = '"
						+ symbol + "'");
	}

	@RequestMapping("/count")
	public long countAllQuotes() {
		return symbols.getSymbols().size();
	}

	@RequestMapping("/indexAverage")
	public float indexAverage() {
		return getIndexInfo().getPrice();
	}

	@RequestMapping("/openAverage")
	public float openAverage() {
		return getIndexInfo().getOpen();
	}

	@RequestMapping("/volume")
	public long volume() {
		return getIndexInfo().getVolume();
	}

	public Quote getIndexInfo() {
		return getIndexInfo(INDEX_SYMBOL);
	}

	@Cacheable("index")
	public Quote getIndexInfo(String symbol) {
		return quoteRepository
				.getQuote("select * from yahoo.finance.quotes where symbol = '"
						+ symbol + "'");
	}

	@RequestMapping("/change")
	public float change() {
		return getIndexInfo().getChange();
	}

	@RequestMapping("/topGainers")
	public List<Quote> topGainers() {
		String s = "select * from yahoo.finance.quotes where symbol in "
				+ QuoteDecoder.formatSymbols(symbols.getSymbols())
				+ " | sort(field=\"Change\", descending=\"true\") | truncate(count=3)";
		return quoteRepository.getQuotes(s);
	}

	@RequestMapping("/topLosers")
	public List<Quote> topLosers() {
		String s = "select * from yahoo.finance.quotes where symbol in "
				+ QuoteDecoder.formatSymbols(symbols.getSymbols())
				+ " | sort(field=\"Change\", descending=\"false\") | truncate(count=3)";
		return quoteRepository.getQuotes(s);
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

	@Cacheable("quotes")
	@RequestMapping("/")
	public List<Quote> findAll() {

		// check to see if we have all of our symbols in the cache
		Cache cache = manager.getCache("quotes");
		cache.evictExpiredElements();

		if (cache.getKeys().size() < countAllQuotes()) {
			// some are not in cache, go get all of them.
			String s = "select * from yahoo.finance.quotes where symbol in "
					+ QuoteDecoder.formatSymbols(symbols.getSymbols())
					+ " | sort(field=\"Symbol\", descending=\"false\")";
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
			l.add((Quote) cache.get(key).getObjectValue());
		}
		return l;
	}
}