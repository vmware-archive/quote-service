package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
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

	private static final String BASE_URI = "http://localhost:9876/quoteService";

	@Autowired
	QuoteController quoteController;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testFindBySymbol() {
		Quote obj = quoteController.findBySymbol("GOOG");
		assertNotNull("Should find a result.", obj);

		// change can be positive, negative or zero, so just make sure nothing
		// is thrown
		try {
			obj.getChange();
		} catch (Throwable t) {
			fail(t.getMessage());
		}
		assertEquals("Google Inc.", obj.getName());
		assertTrue(obj.getDaysHigh() > 0.0);
		assertTrue(obj.getDaysLow() > 0);
		assertTrue(obj.getOpen() > 0);
		assertTrue(obj.getPrice() > 0);
		assertEquals("GOOG", obj.getSymbol());
		assertTrue(obj.getVolume() > 0);
		assertTrue(obj.getCreated() != "");
		assertTrue(obj.getAsk() > 0);
		assertTrue(obj.getBid() > 0);
		assertTrue(obj.getPercentageChange() != "");
		assertTrue(obj.getPreviousClose() > 0);

		// non-existent symbols
		assertNull(quoteController.findBySymbol(null));
		assertNull(quoteController.findBySymbol(""));
		assertNull(quoteController.findBySymbol("IBM"));
	}

	@Test
	public void testFindBySymbolIn() {
		Set<String> s = new HashSet<String>();
		s.add("AMZN");
		s.add("EBAY");
		List<Quote> res = quoteController.findBySymbolIn(s);
		assertNotNull(res);
		assertTrue("Should have two results.", res.size() == 2);
		assertTrue("AMZN".equals(res.get(0).getSymbol())
				|| "EBAY".equals(res.get(0).getSymbol()));
		assertTrue("AMZN".equals(res.get(1).getSymbol())
				|| "EBAY".equals(res.get(1).getSymbol()));

		s.add(null);
		res = quoteController.findBySymbolIn(s);
		assertTrue("Should have two results.", res.size() == 2);

		s.add("");
		res = quoteController.findBySymbolIn(s);
		assertTrue("Should have two results.", res.size() == 2);

		s.add("IBM");
		res = quoteController.findBySymbolIn(s);
		assertTrue("Should have two results.", res.size() == 2);
	}

	@Test
	public void testFindIndexAverage() {
		float f = quoteController.indexAverage();
		assertTrue(f > 0.0f);
	}

	@Test
	public void testFindOpenAverage() {
		float f = quoteController.openAverage();
		assertTrue(f > 0.0f);
	}

	@Test
	public void testFindVolume() {
		long l = quoteController.volume();
		assertTrue(l > 0);
	}

	@Test
	public void testFindChange() {
		try {
			quoteController.change();
		} catch (Throwable t) {
			// can be any number, positive, negative or zero, so just looking
			// for "no exception."
			fail(t.getMessage());
		}
	}

	@Test
	public void testMarketSummary() {
		Map<String, Object> m = quoteController.marketSummary();
		assertNotNull(m);
		assertTrue(m.size() == 5);
		assertNotNull(m.get("tradeStockIndexAverage"));
		assertNotNull(m.get("tradeStockIndexOpenAverage"));
		assertNotNull(m.get("tradeStockIndexVolume"));
		assertNotNull(m.get("cnt"));
		assertNotNull(m.get("change"));
	}

	@Test
	public void testGainers() {
		List<Quote> qs = quoteController.topGainers();
		assertNotNull(qs);
		assertEquals(3, qs.size());
		for (Quote q : qs) {
			assertNotNull(q);
			assertNotNull(q.getSymbol());
			assertNotNull(q.getChange());
		}
	}

	@Test
	public void testLosers() {
		List<Quote> qs = quoteController.topLosers();
		assertNotNull(qs);
		assertEquals(3, qs.size());
		for (Quote q : qs) {
			assertNotNull(q);
			assertNotNull(q.getSymbol());
			assertNotNull(q.getChange());
		}
	}

	@Test
	public void testCountAllQuotes() {
		assertEquals("22", "" + quoteController.countAllQuotes());
	}

	@Test
	public void testFindAll() {
		List<Quote> all = quoteController.findAll();
		assertEquals("22", "" + all.size());
		Quote q = all.get(0);
		assertNotNull(q);
		assertEquals(q.getSymbol(), "AAPL");
		q = all.get(21);
		assertNotNull(q);
		assertEquals(q.getSymbol(), "YHOO");
	}

	@Test
	@Ignore
	public void testResponseFndById() throws Exception {
		ResponseEntity<Quote> qr = restTemplate.getForEntity(BASE_URI
				+ "/findBySymbol/GOOG", Quote.class);
		assertNotNull(qr);
		assertEquals(HttpStatus.OK, qr.getStatusCode());
		Quote q = qr.getBody();
		assertEquals("GOOG", q.getSymbol());

		qr = restTemplate.getForEntity(BASE_URI + "/findBySymbol/abc",
				Quote.class);
		assertNotNull(qr);
		assertEquals(HttpStatus.NOT_FOUND, qr.getStatusCode());

		qr = restTemplate
				.getForEntity(BASE_URI + "/findBySymbol/", Quote.class);
		assertNotNull(qr);
		assertEquals(HttpStatus.BAD_REQUEST, qr.getStatusCode());
	}
}
