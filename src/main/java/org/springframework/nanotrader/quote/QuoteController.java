/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.nanotrader.quote;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quote")
@Transactional
public class QuoteController {

	@Autowired
	QuoteRepository quoteRepository;

	@RequestMapping("/count")
	public long countAllQuotes() {
		return quoteRepository.count();
	}

	@RequestMapping("/delete/{quote}")
	public void deleteQuote(@RequestParam Quote quote) {
		quoteRepository.delete(quote);
	}

	@RequestMapping("/findQuote/{id}")
	public ResponseEntity<Quote> findQuote(@RequestParam Integer id) {
		Quote quote = quoteRepository.findOne(id);
		if(quote == null) {
			return new ResponseEntity<Quote>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Quote>(quote, HttpStatus.OK);
	}

	@RequestMapping("/findAllQuotes")
	public Iterable<Quote> findAllQuotes() {
		return quoteRepository.findAll();
	}

	@RequestMapping("/findQuoteEntries")
	public List<Quote> findQuoteEntries(int firstResult, int maxResults) {
		return quoteRepository.findAll(
				new PageRequest(firstResult / maxResults, maxResults))
				.getContent();
	}

	@RequestMapping("/saveQuote/{quote}")
	public void saveQuote(@RequestParam Quote quote) {
		quoteRepository.save(quote);
	}

	@RequestMapping("/updateQuote/{quote}")
	public Quote updateQuote(@RequestParam Quote quote) {
		return quoteRepository.save(quote);
	}

	@RequestMapping("/findBySymbol/{symbol}")
	public ResponseEntity<Quote> findBySymbol(@RequestParam String symbol) {
		Quote quote = quoteRepository.findBySymbol(symbol);
		if(quote == null) {
			return new ResponseEntity<Quote>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Quote>(quote, HttpStatus.OK);
	}

	@RequestMapping("/findBySymbolIn/{symbols}")
	public List<Quote> findBySymbolIn(@RequestParam Set<String> symbols) {
		return quoteRepository.findBySymbolIn(symbols);
	}

	@RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
	public ResponseEntity<Quote> findQuote(@PathVariable("symbol") String symbol) {
		return findBySymbol(symbol);
	}

	// @RequestMapping(value = "/quotes", method = RequestMethod.GET)
	// @ResponseBody
	// public CollectionResult findQuotes() {
	// return getTradingServiceFacade().findQuotes();
	// }

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public void post() {
	}

	@RequestMapping(value = "/{symbol}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public void put() {

	}

	@RequestMapping(value = "/{symbol}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public void delete() {

	}

}
