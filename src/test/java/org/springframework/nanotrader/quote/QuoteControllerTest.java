package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class QuoteControllerTest {

	@Autowired
	QuoteController quoteController;

	@Test
	public void testCountAllQuotes() {
		long count = quoteController.countAllQuotes();
		assertTrue(
				"Counter for 'Quote' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindQuote() {
		Quote obj = quoteController.findQuote(1);
		assertNotNull(
				"Find method for 'Quote' illegally returned null for id '"
						+ new Integer(1) + "'", obj);
		assertEquals(
				"Find method for 'Quote' returned the incorrect identifier",
				new Integer(1), obj.getQuoteid());
	}

	@Test
	public void testFindAllQuotes() {
		Iterable<Quote> result = quoteController.findAllQuotes();
		assertNotNull("Find all method for 'Quote' illegally returned null",
				result);
	}

	@Test
	public void testFindQuoteEntries() {
		long count = quoteController.countAllQuotes();
		if (count > 20)
			count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Quote> result = quoteController.findQuoteEntries(firstResult,
				maxResults);
		assertNotNull(
				"Find entries method for 'Quote' illegally returned null",
				result);
		assertEquals(
				"Find entries method for 'Quote' returned an incorrect number of entries",
				count, result.size());
	}

	@Test
	public void testSaveQuote() {
		Quote obj = new Quote();
		obj.setSymbol("BAZZ");
		obj.setVolume(new BigDecimal(123));
		obj.setChange1(new BigDecimal(234));
		quoteController.saveQuote(obj);
		assertNotNull("Expected 'Quote' identifier to no longer be null",
				obj.getQuoteid());
	}

	@Test
	public void testUpdateQuote() {
		Quote obj = quoteController.findQuote(12);
		String symbol = obj.getSymbol();
		obj.setSymbol("FOO");
		quoteController.updateQuote(obj);

		obj = quoteController.findQuote(12);
		assertEquals("FOO", obj.getSymbol());
		assertFalse("Symbol should have changed.",
				obj.getSymbol().equals(symbol));
	}

	@Test
	public void testDeleteQuote() {
		Quote obj = quoteController.findQuote(2);
		assertNotNull(obj);
		quoteController.deleteQuote(2);
		obj = quoteController.findQuote(2);
		assertNull(obj);
	}

	@Test
	public void testFindBySymbol() {
		Quote obj = quoteController.findBySymbol("BRCM");
		assertNotNull("Should find a result.", obj);
	}

	@Test
	public void testFindBySymbolIn() {
		Set<String> s = new HashSet<String>();
		s.add("BRCM");
		s.add("EBAY");
		List<Quote> res = quoteController.findBySymbolIn(s);
		assertNotNull(res);
		assertTrue("Should have two results.", res.size() == 2);
	}

	@Test
	public void testFindByPage() {
		List<Quote> resp = quoteController.findAll(3, 20);
		assertNotNull(resp);
		assertTrue(resp.size() == 20);
	}

	@Test
	public void testFindIndexAverage() {
		Long l = quoteController.indexAverage();
		assertNotNull(l);
		assertEquals(107, l.longValue());
	}

	@Test
	public void testFindOpenAverage() {
		Long l = quoteController.openAverage();
		assertNotNull(l);
		assertEquals(104, l.longValue());
	}

	@Test
	public void testFindVolume() {
		assertTrue(458 == quoteController.volume());
	}

	@Test
	public void testFindChange() {
		Long l = quoteController.change();
		assertNotNull(l);
		assertEquals(1042, l.longValue());
	}

	@Test
	public void testMarketSummary() {
		Map<String, Long> m = quoteController.marketSummary();
		assertNotNull(m);
		assertTrue(m.size() == 5);
	}

	@Test
	public void testLosers() {
		List<Quote> q = quoteController.topLosers();
		assertNotNull(q);
		assertEquals(3, q.size());
		assertEquals("ATVI", q.get(0).getSymbol());
		assertEquals("CHKP", q.get(1).getSymbol());
		assertEquals("EA", q.get(2).getSymbol());
	}

	@Test
	public void testGainers() {
		List<Quote> q = quoteController.topGainers();
		assertNotNull(q);
		assertEquals(3, q.size());
		assertEquals("AAPL", q.get(0).getSymbol());
		assertEquals("CSCO", q.get(1).getSymbol());
		assertEquals("DLTR", q.get(2).getSymbol());
	}
}
