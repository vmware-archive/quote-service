package org.springframework.nanotrader.quote;

import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
	public Quote findBySymbol(@PathVariable String symbol) {
		if (symbol == null || symbol.length() < 1) {
			return null;
		}
		return getQuote(symbol);
	}

	private Quote getIndexInfo() {
		return getQuote(INDEX_SYMBOL);
	}

	private Quote getQuote(String symbol) {
		return quoteRepository
				.getQuote("select * from yahoo.finance.quotes where symbol = '"
						+ symbol + "'");
	}

	@RequestMapping("/marketSummary")
	public MarketSummary marketSummary() {
		return new MarketSummary(getIndexInfo());
	}
}