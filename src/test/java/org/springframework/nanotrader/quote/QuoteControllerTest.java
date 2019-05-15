package org.springframework.nanotrader.quote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
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
		assertEquals("Alphabet Inc.", obj.getName());
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
	public void testMarketSummary() {
		MarketSummary m = quoteController.marketSummary();
		assertNotNull(m);
		assertTrue(m.getAverage() != 0.0f);
		assertTrue(m.getOpen() != 0.0f);
		assertTrue(m.getVolume() != 0.0f);
		assertTrue(m.getChange() != 0.0f);
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
