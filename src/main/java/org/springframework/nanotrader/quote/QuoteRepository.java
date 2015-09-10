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

	@RequestLine("GET /yql?q={query}&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
	public Quote getQuote(@Param("query") String query);

	@RequestLine("GET /yql?q={query}&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
	List<Quote> getQuotes(@Param("query") String query);
}