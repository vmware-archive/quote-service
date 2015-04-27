package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class QuoteIntegrationTest {

	@Autowired
	QuoteService quoteService;

	@Test
	public void testCountAllQuotes() {
		long count = quoteService.countAllQuotes();
		assertTrue(
				"Counter for 'Quote' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testFindQuote() {
		Quote obj = quoteService.findQuote(1);
		assertNotNull(
				"Find method for 'Quote' illegally returned null for id '"
						+ new Integer(1) + "'", obj);
		assertEquals(
				"Find method for 'Quote' returned the incorrect identifier",
				new Integer(1), obj.getQuoteid());
	}

	@Test
	public void testFindAllQuotes() {
		Iterable<Quote> result = quoteService.findAllQuotes();
		assertNotNull("Find all method for 'Quote' illegally returned null",
				result);
	}

	@Test
	public void testFindQuoteEntries() {
		long count = quoteService.countAllQuotes();
		if (count > 20)
			count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Quote> result = quoteService.findQuoteEntries(firstResult,
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
		quoteService.saveQuote(obj);
		assertNotNull("Expected 'Quote' identifier to no longer be null",
				obj.getQuoteid());
	}

	@Test
	public void testUpdateQuote() {
		Quote obj = quoteService.findQuote(12);
		String symbol = obj.getSymbol();
		obj.setSymbol("FOO");
		quoteService.updateQuote(obj);

		obj = quoteService.findQuote(12);
		assertEquals("FOO", obj.getSymbol());
		assertFalse("Symbol should have changed.",
				obj.getSymbol().equals(symbol));
	}

	@Test
	public void testDeleteQuote() {
		Quote obj = quoteService.findQuote(2);
		quoteService.deleteQuote(obj);
		assertNull("Failed to remove 'Quote' with identifier '" + 2 + "'",
				quoteService.findQuote(2));
	}

	@Test
	public void testFindBySymbol() {
		Quote obj = quoteService.findBySymbol("BRCM");
		assertNotNull("Should find a result.", obj);
	}

	@Test
	public void testFindBySymbolIn() {
		Set<String> s = new HashSet<String>();
		s.add("BRCM");
		s.add("EBAY");
		List<Quote> res = quoteService.findBySymbolIn(s);
		assertNotNull(res);
		assertTrue("Should have two results.", res.size() == 2);
	}
}
