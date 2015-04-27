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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quoteService")
@Transactional
public class QuoteService {

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
	public Quote findQuote(@RequestParam Integer id) {
		return quoteRepository.findOne(id);
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
	public Quote findBySymbol(@RequestParam String symbol) {
		return quoteRepository.findBySymbol(symbol);
	}

	@RequestMapping("/findBySymbolIn/{symbols}")
	public List<Quote> findBySymbolIn(@RequestParam Set<String> symbols) {
		return quoteRepository.findBySymbolIn(symbols);
	}	
}
