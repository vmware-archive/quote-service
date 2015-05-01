package org.springframework.nanotrader.quote;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author jgordon
 */
public interface QuoteRepository extends
		PagingAndSortingRepository<Quote, Integer> {

	Quote findBySymbol(String symbol);

	List<Quote> findBySymbolIn(Set<String> symbols);
}