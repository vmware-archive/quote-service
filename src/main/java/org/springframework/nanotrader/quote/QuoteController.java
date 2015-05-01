package org.springframework.nanotrader.quote;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quoteService")
public class QuoteController {

	@Autowired
	QuoteRepository quoteRepository;

	@RequestMapping("/count")
	public long countAllQuotes() {
		return quoteRepository.count();
	}

	@RequestMapping("/delete/{id}")
	public void deleteQuote(@PathVariable int id) {
		quoteRepository.delete(id);
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public Quote findQuote(@PathVariable int id) {
		return quoteRepository.findOne(id);
	}

	@RequestMapping("/findAll")
	public Iterable<Quote> findAllQuotes() {
		return quoteRepository.findAll();
	}

	@RequestMapping("/findQuoteEntries")
	public List<Quote> findQuoteEntries(@RequestParam int firstResult,
			@RequestParam int maxResults) {
		return quoteRepository.findAll(
				new PageRequest(firstResult / maxResults, maxResults))
				.getContent();
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Quote saveQuote(@RequestBody Quote quote) {
		return quoteRepository.save(quote);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Quote updateQuote(@RequestBody Quote quote) {
		return quoteRepository.save(quote);
	}

	@RequestMapping("/findBySymbol/{symbol}")
	public Quote findBySymbol(@PathVariable String symbol) {
		return quoteRepository.findBySymbol(symbol);
	}

	@RequestMapping("/findBySymbolIn")
	public List<Quote> findBySymbolIn(@RequestParam Set<String> symbols) {
		return quoteRepository.findBySymbolIn(symbols);
	}

	@RequestMapping("/findAllPaged")
	public List<Quote> findAll(@RequestParam int page, @RequestParam int size) {
		return quoteRepository.findAll(new PageRequest(page, size))
				.getContent();
	}

}
