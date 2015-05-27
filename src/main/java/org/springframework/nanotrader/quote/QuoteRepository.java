package org.springframework.nanotrader.quote;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author jgordon
 */
public interface QuoteRepository extends
		PagingAndSortingRepository<Quote, Integer> {

	Quote findBySymbol(String symbol);

	List<Quote> findBySymbolIn(Set<String> symbols);

	@Query("SELECT SUM(q.price)/COUNT(q) FROM Quote q")
	Long findIndexAverage();

	@Query("SELECT SUM(q.open1)/COUNT(q) FROM Quote q")
	Long findOpenAverage();

	@Query("SELECT SUM(q.volume) FROM Quote q")
	Long findVolume();

	@Query("SELECT SUM(q.change1) FROM Quote q")
	Long findChange();

	@Query("SELECT SUM(q.change1) FROM Quote q")
	List<Quote> topGainers();

	List<Quote> findAllByOrderByChange1Desc(Pageable pageable);

	List<Quote> findAllByOrderByChange1Asc(Pageable pageable);
}