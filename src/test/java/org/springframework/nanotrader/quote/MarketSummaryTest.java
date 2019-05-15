package org.springframework.nanotrader.quote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketSummaryTest {

	@Test
	public void testPercents() {
		Quote q = new Quote();
		q.setPercentageChange("+12.3%");
		MarketSummary m = new MarketSummary(q);
		assertEquals("12.3", "" + m.getPercentGain());

		q.setPercentageChange("+12.4");
		m = new MarketSummary(q);
		assertEquals("12.4", "" + m.getPercentGain());

		q.setPercentageChange("12.5");
		m = new MarketSummary(q);
		assertEquals("12.5", "" + m.getPercentGain());

		q.setPercentageChange("");
		m = new MarketSummary(q);
		assertEquals("0.0", "" + m.getPercentGain());

		q.setPercentageChange(null);
		m = new MarketSummary(q);
		assertEquals("0.0", "" + m.getPercentGain());

		q.setPercentageChange("-3.45%");
		m = new MarketSummary(q);
		assertEquals("-3.45", "" + m.getPercentGain());

		q.setPercentageChange("-3.46");
		m = new MarketSummary(q);
		assertEquals("-3.46", "" + m.getPercentGain());
	}
}
