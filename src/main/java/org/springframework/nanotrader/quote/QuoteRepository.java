package org.springframework.nanotrader.quote;

import java.util.List;

import org.springframework.stereotype.Repository;

import feign.Param;
import feign.RequestLine;

/**
 * @author jgordon
 */
@Repository
public interface QuoteRepository {

	@RequestLine("GET stock/{query}/quote")
	public Quote getQuote(@Param("query") String query);

	@RequestLine("GET /stock/market/batch?symbols={query}&types=quote")
	public List<Quote> getQuotes(@Param("query") String query);
}