package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@WebIntegrationTest(value = "server.port=9876")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
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
