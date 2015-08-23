package org.springframework.nanotrader.quote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quoteService")
public class QuoteController {

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	Symbols symbols;

	@RequestMapping("/findBySymbol/{symbol}")
	public Quote findBySymbol(@PathVariable String symbol) {
		if (symbols.exists(symbol)) {
			return quoteRepository
					.findBySymbol("select * from yahoo.finance.quotes where symbol = '"
							+ symbol + "'");
		}
		return null;
	}

	@RequestMapping("/findBySymbolIn/{set}")
	public List<Quote> findBySymbolIn(@RequestParam Set<String> set) {
		Set<String> s = symbols.checkSymbols(set);
		if (s.size() < 1) {
			return new ArrayList<Quote>();
		}

		if (s.size() < 2) {
			List<Quote> l = new ArrayList<Quote>();
			l.add(findBySymbol(set.toArray()[0].toString()));
			return l;
		}

		return quoteRepository
				.findBySymbolIn("select * from yahoo.finance.quotes where symbol in "
						+ QuoteDecoder.formatSymbols(s));
	}

	@RequestMapping("/count")
	public long countAllQuotes() {
		return symbols.getSymbols().size();
	}

	@RequestMapping("/indexAverage")
	public float indexAverage() {
		return getSPIndexInfo().getPrice();
	}

	@RequestMapping("/openAverage")
	public float openAverage() {
		return getSPIndexInfo().getOpen();
	}

	@RequestMapping("/volume")
	public long volume() {
		return getSPIndexInfo().getVolume();
	}

	private Quote getSPIndexInfo() {
		return quoteRepository
				.findQuote("select * from yahoo.finance.quotes where symbol in (\"^GSPC\")");
	}

	@RequestMapping("/change")
	public float change() {
		return getSPIndexInfo().getChange();
	}

	@RequestMapping("/topGainers")
	public List<Quote> topGainers() {
		String s = "select * from yahoo.finance.quotes where symbol in "
				+ QuoteDecoder.formatSymbols(symbols.getSymbols())
				+ " | sort(field=\"Change\", descending=\"true\") | truncate(count=3)";
		return quoteRepository.findQuotes(s);
	}

	@RequestMapping("/topLosers")
	public List<Quote> topLosers() {
		String s = "select * from yahoo.finance.quotes where symbol in "
				+ QuoteDecoder.formatSymbols(symbols.getSymbols())
				+ " | sort(field=\"Change\", descending=\"false\") | truncate(count=3)";
		return quoteRepository.findQuotes(s);
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

	@RequestMapping("/findAll")
	public List<Quote> findAll() {
		String s = "select * from yahoo.finance.quotes where symbol in "
				+ QuoteDecoder.formatSymbols(symbols.getSymbols())
				+ " | sort(field=\"Symbol\", descending=\"false\")";
		return quoteRepository.findQuotes(s);
	}
}