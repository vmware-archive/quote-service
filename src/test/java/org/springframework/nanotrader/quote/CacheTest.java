package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@WebIntegrationTest(value = "server.port=9876")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class CacheTest {

	@Autowired
	QuoteController quoteController;

	@Autowired
	CacheManager manager;

	@Test
	public void testQuotesCache() {
		Cache cache = manager.getCache("quotes");
		cache.removeAll();
		assertTrue(cache.getKeys().size() == 0);

		assertEquals(0, cache.getStatistics().cacheHitCount());
		assertEquals(0, cache.getStatistics().cacheMissCount());

		Quote q = quoteController.findBySymbol("GOOG");
		assertNotNull(q);
		assertEquals(0, cache.getStatistics().cacheHitCount());
		assertEquals(1, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("GOOG");
		assertNotNull(q);
		assertEquals(1, cache.getStatistics().cacheHitCount());
		assertEquals(1, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("YHOO");
		assertNotNull(q);
		assertEquals(1, cache.getStatistics().cacheHitCount());
		assertEquals(2, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("YHOO");
		assertNotNull(q);
		assertEquals(2, cache.getStatistics().cacheHitCount());
		assertEquals(2, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("IBM");
		assertNull(q);
		assertEquals(2, cache.getStatistics().cacheHitCount());
		assertEquals(3, cache.getStatistics().cacheMissCount());
		System.out.println(cache.getKeys());
	}

	@Test
	public void testQuotesCacheFindAll() {
		Cache cache = manager.getCache("quotes");
		cache.removeAll();
		assertTrue(cache.getKeys().size() == 0);

		// long hits = cache.getStatistics().cacheHitCount();
		// assertEquals(0, hits);

		// long misses = cache.getStatistics().cacheMissCount();
		// assertEquals(0, misses);

		// long puts = cache.getStatistics().cachePutCount();

		List<Quote> l = quoteController.findAll();
		assertNotNull(l);
		assertEquals(22, l.size());
		// assertEquals(puts + 22, cache.getStatistics().cachePutCount());
		// assertEquals(hits, cache.getStatistics().cacheHitCount());
		// assertEquals(1, cache.getStatistics().cacheMissCount());

		// Quote q = quoteController.findBySymbol("IBM");
		// assertNull(q);
		// assertEquals(0, cache.getStatistics().cacheHitCount());
		// assertEquals(1, cache.getStatistics().cacheMissCount());

		long puts = cache.getStatistics().cachePutCount();

		l = quoteController.findAll();
		assertNotNull(l);
		assertEquals(22, l.size());
		assertEquals(puts, cache.getStatistics().cachePutCount());
	}

	@Test
	public void testIndexCache() {
		Cache cache = manager.getCache("index");
		cache.removeAll();
		assertTrue(cache.getKeys().size() == 0);
		assertEquals(0, cache.getStatistics().cacheHitCount());
		assertEquals(0, cache.getStatistics().cacheMissCount());

		Quote q = quoteController.getIndexInfo(QuoteController.INDEX_SYMBOL);
		assertNotNull(q);
		// System.out.println(cache.getKeys());
		assertEquals(0, cache.getStatistics().cacheHitCount());
		assertEquals(1, cache.getStatistics().cacheMissCount());

		q = quoteController.getIndexInfo(QuoteController.INDEX_SYMBOL);
		assertNotNull(q);
		assertEquals(1, cache.getStatistics().cacheHitCount());
		assertEquals(1, cache.getStatistics().cacheMissCount());

		long puts = cache.getStatistics().cachePutCount();
		float f = quoteController.change();
		assertTrue(f != 0.0f);
		assertEquals(puts, cache.getStatistics().cachePutCount());
	}
}
