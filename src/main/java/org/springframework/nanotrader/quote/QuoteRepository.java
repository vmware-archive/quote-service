package org.springframework.nanotrader.quote;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jgordon
 */

@Repository
public interface QuoteRepository extends
		PagingAndSortingRepository<Quote, String> {

	List<Quote> findAllByOrderByChangeDesc(Pageable pageable);

	List<Quote> findAllByOrderByChangeAsc(Pageable pageable);

}