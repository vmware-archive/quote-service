package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@WebIntegrationTest(value = "server.port=9876")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class QuoteControllerTest {

	private static final String BASE_URI = "http://localhost:9876/quotes";

	@Autowired
	QuoteController quoteController;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testFindQuote() {
		Quote obj = quoteController.findBySymbol("GOOG");
		assertNotNull(obj);
		assertEquals("GOOG", obj.getSymbol());
	}

	@Test
	public void testFindAllQuotes() {
		Iterable<Quote> result = quoteController.findAllQuotes();
		assertNotNull("Find all method for 'Quote' illegally returned null",
				result);
	}

	@Test
	public void testSaveAndDeleteQuote() {
		Quote obj = new Quote();
		obj.setSymbol("BAZZ");
		obj.setVolume(123);
		obj.setChange1(234);
		Quote q2 = quoteController.saveQuote(obj);
		assertEquals("BAZZ", obj.getSymbol());

		quoteController.deleteQuote(q2);
		assertNull(quoteController.findBySymbol(q2.getSymbol()));
	}

	@Test
	public void testFindBySymbolIn() {
		Set<String> s = new HashSet<String>();
		s.add("YHOO");
		s.add("EBAY");
		List<Quote> res = quoteController.findBySymbolIn(s);
		assertNotNull(res);
		assertTrue("Should have two results.", res.size() == 2);
	}

	@Test
	public void testMarketSummary() {
		MarketSummary m = quoteController.marketSummary();
		assertNotNull(m);
		assertTrue(m.getChange() != 0);
		assertTrue(m.getPercentGain() != 0);
		assertNotNull(m.getSummaryDate());
		assertTrue(m.getTopGainers().size() == 3);
		assertTrue(m.getTopLosers().size() == 3);
		assertTrue(m.getTradeStockIndexAverage() != 0);
		assertTrue(m.getTradeStockIndexOpenAverage() != 0);
		assertTrue(m.getTradeStockIndexVolume() != 0);
	}

	@Test
	public void testLosers() {
		List<Quote> q = quoteController.topLosers();
		assertNotNull(q);
		assertEquals(3, q.size());
		assertEquals("EA", q.get(0).getSymbol());
		assertEquals("AMZN", q.get(1).getSymbol());
		assertEquals("VRSN", q.get(2).getSymbol());
	}

	@Test
	public void testGainers() {
		List<Quote> q = quoteController.topGainers();
		assertNotNull(q);
		assertEquals(3, q.size());
		assertEquals("AAPL", q.get(0).getSymbol());
		assertEquals("CSCO", q.get(1).getSymbol());
		assertEquals("INTC", q.get(2).getSymbol());
	}

	@Test
	public void testCountAllQuotes() {
		long count = quoteController.countAllQuotes();
		assertTrue(
				"Counter for 'Quote' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testResponseFndById() throws Exception {
		ResponseEntity<Quote> qr = restTemplate.getForEntity(BASE_URI
				+ "/findBySymbol/YHOO", Quote.class);
		assertNotNull(qr);
		assertEquals(HttpStatus.OK, qr.getStatusCode());
		Quote q = qr.getBody();
		assertEquals("YHOO", q.getSymbol());

		qr = restTemplate
				.getForEntity(BASE_URI + "/findBySymbol/", Quote.class);
		assertNotNull(qr);
		assertEquals(HttpStatus.NOT_FOUND, qr.getStatusCode());
	}
}
