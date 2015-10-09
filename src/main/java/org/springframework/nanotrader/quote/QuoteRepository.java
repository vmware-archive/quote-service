package org.springframework.nanotrader.quote;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jgordon
 */
@Repository
public interface QuoteRepository extends
		PagingAndSortingRepository<Quote, String> {

	Quote findBySymbol(String symbol);

	List<Quote> findBySymbolIn(Set<String> symbols);

	@Query("SELECT SUM(q.price)/COUNT(q) FROM Quote q")
	Float findIndexAverage();

	@Query("SELECT SUM(q.open)/COUNT(q) FROM Quote q")
	Float findOpenAverage();

	@Query("SELECT SUM(q.volume) FROM Quote q")
	Long findVolume();

	List<Quote> findAllByOrderByChangeDesc(Pageable pageable);

	List<Quote> findAllByOrderByChangeAsc(Pageable pageable);
}