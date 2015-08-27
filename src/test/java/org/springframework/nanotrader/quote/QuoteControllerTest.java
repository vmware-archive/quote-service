package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
public class QuoteControllerTest {

	@Autowired
	QuoteController quoteController;

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

	@SuppressWarnings("unchecked")
	@Test
	public void testMarketSummary() {
		Map<String, Object> m = quoteController.marketSummary();
		assertNotNull(m);
		assertEquals(6, m.size());
		assertNotNull(m.get("average"));
		assertNotNull(m.get("open"));
		assertNotNull(m.get("volume"));
		assertNotNull(m.get("change"));
		List<Quote> qs = (List<Quote>) m.get("topGainers");
		assertNotNull(qs);
		assertEquals(3, qs.size());
		for (Quote q : qs) {
			assertNotNull(q);
			assertNotNull(q.getSymbol());
			assertNotNull(q.getChange());
		}

		qs = (List<Quote>) m.get("topLosers");
		assertNotNull(qs);
		assertEquals(3, qs.size());
		for (Quote q : qs) {
			assertNotNull(q);
			assertNotNull(q.getSymbol());
			assertNotNull(q.getChange());
		}

	}

	@Test
	public void testFindAll() {
		List<Quote> all = quoteController.findAll();
		assertEquals("22", "" + all.size());
	}

	@Test
	public void testDescComparator() {
		List<Quote> l = quoteController.findAll();
		Collections.sort(l, new DescendingChangeComparator());
		float f = Float.MAX_VALUE;
		for (Quote q : l) {
			assertTrue(f + " is greater than " + q.getChange(),
					f >= q.getChange());
			f = q.getChange();
		}
	}

	@Test
	public void testAscComparator() {
		List<Quote> l = quoteController.findAll();
		Collections.sort(l, new AscendingChangeComparator());
		float f = -Float.MAX_VALUE;
		for (Quote q : l) {
			assertTrue(f + " is less than " + q.getChange(), f <= q.getChange());
			f = q.getChange();
		}
	}
}
