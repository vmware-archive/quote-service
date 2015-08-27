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

		long hits = cache.getStatistics().cacheHitCount();
		long misses = cache.getStatistics().cacheMissCount();

		Quote q = quoteController.findBySymbol("GOOG");
		assertNotNull(q);
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(++misses, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("GOOG");
		assertNotNull(q);
		assertEquals(++hits, cache.getStatistics().cacheHitCount());
		assertEquals(misses, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("YHOO");
		assertNotNull(q);
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(++misses, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("YHOO");
		assertNotNull(q);
		assertEquals(++hits, cache.getStatistics().cacheHitCount());
		assertEquals(misses, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("IBM");
		assertNull(q);
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(++misses, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol(null);
		assertNull(q);
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(++misses, cache.getStatistics().cacheMissCount());

		q = quoteController.findBySymbol("");
		assertNull(q);
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(++misses, cache.getStatistics().cacheMissCount());
	}

	@Test
	public void testQuotesCacheFindAll() {
		Cache cache = manager.getCache("quotes");
		cache.removeAll();
		assertTrue(cache.getKeys().size() == 0);

		long hits = cache.getStatistics().cacheHitCount();
		long misses = cache.getStatistics().cacheMissCount();
		long puts = cache.getStatistics().cachePutCount();

		List<Quote> l = quoteController.findAll();
		assertNotNull(l);
		assertEquals(22, l.size());
		assertEquals(puts + 22, cache.getStatistics().cachePutCount());
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(misses, cache.getStatistics().cacheMissCount());

		Quote q = quoteController.findBySymbol("IBM");
		assertNull(q);
		assertEquals(hits, cache.getStatistics().cacheHitCount());
		assertEquals(++misses, cache.getStatistics().cacheMissCount());

		puts = cache.getStatistics().cachePutCount();
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

		MarketSummary m = quoteController.marketSummary();
		assertNotNull(m);
		assertEquals(0, cache.getStatistics().cacheHitCount());
		assertEquals(1, cache.getStatistics().cacheMissCount());

		m = quoteController.marketSummary();
		assertNotNull(m);
		assertEquals(1, cache.getStatistics().cacheHitCount());
		assertEquals(1, cache.getStatistics().cacheMissCount());
	}
}
