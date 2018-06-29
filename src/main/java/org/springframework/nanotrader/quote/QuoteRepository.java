package org.springframework.nanotrader.quote;

import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository {

    @RequestLine("GET stock/{query}/quote")
    public Quote getQuote(@Param("query") String query);

    @RequestLine("GET /stock/market/batch?symbols={query}&types=quote")
    public List<Quote> getQuotes(@Param("query") String query);
}