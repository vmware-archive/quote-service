package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@WebIntegrationTest(value = "server.port=9876")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class QuoteRestTest {

	private static final String BASE_URI = "http://localhost:9876/quotes";

	@Autowired
	QuoteController quoteController;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testFindBySymbol() {
		ResponseEntity<Quote> qr = restTemplate.getForEntity(
				BASE_URI + "/GOOG", Quote.class);

		assertNotNull("Should find a result.", qr);
		Quote q = qr.getBody();
		assertNotNull(q);
		assertEquals("GOOG", q.getSymbol());
		assertEquals("Google Inc.", q.getName());
	}

	@Test
	public void testFindIndexAverage() {
		ResponseEntity<Float> fr = restTemplate.getForEntity(BASE_URI
				+ "/indexAverage", Float.class);
		assertNotNull("Should find a result.", fr);
		Float f = fr.getBody();
		assertNotNull(f);
		assertTrue(f > 0.0f);
	}

	@Test
	public void testFindOpenAverage() {
		ResponseEntity<Float> fr = restTemplate.getForEntity(BASE_URI
				+ "/openAverage", Float.class);
		assertNotNull("Should find a result.", fr);
		Float f = fr.getBody();
		assertNotNull(f);
		assertTrue(f > 0.0f);
	}

	@Test
	public void testFindVolume() {
		ResponseEntity<Float> fr = restTemplate.getForEntity(BASE_URI
				+ "/volume", Float.class);
		assertNotNull("Should find a result.", fr);
		Float f = fr.getBody();
		assertNotNull(f);
		assertTrue(f > 0.0f);
	}

	@Test
	public void testFindChange() {
		ResponseEntity<Float> fr = restTemplate.getForEntity(BASE_URI
				+ "/change", Float.class);
		assertNotNull("Should find a result.", fr);
		Float f = fr.getBody();
		assertNotNull(f);
	}

	@Test
	public void testMarketSummary() {
		ResponseEntity<Map<String, Object>> mr = restTemplate.exchange(BASE_URI
				+ "/marketSummary", HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});

		assertNotNull("Should find a result.", mr);
		Map<String, Object> m = mr.getBody();
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
		ResponseEntity<List<Quote>> qr = restTemplate.exchange(BASE_URI
				+ "/topGainers", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Quote>>() {
				});

		assertNotNull("Should find a result.", qr);
		List<Quote> l = qr.getBody();
		assertNotNull(l);
		assertEquals(3, l.size());
		for (Quote q : l) {
			assertNotNull(q);
			assertNotNull(q.getSymbol());
			assertNotNull(q.getChange());
		}
	}

	@Test
	public void testLosers() {
		ResponseEntity<List<Quote>> qr = restTemplate.exchange(BASE_URI
				+ "/topLosers", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Quote>>() {
				});

		assertNotNull("Should find a result.", qr);
		List<Quote> l = qr.getBody();
		assertNotNull(l);
		assertEquals(3, l.size());
		for (Quote q : l) {
			assertNotNull(q);
			assertNotNull(q.getSymbol());
			assertNotNull(q.getChange());
		}
	}

	@Test
	public void testCountAllQuotes() {
		ResponseEntity<Long> lr = restTemplate.getForEntity(
				BASE_URI + "/count", Long.class);
		assertNotNull("Should find a result.", lr);
		Long l = lr.getBody();
		assertNotNull(l);
		assertEquals(22l, l.longValue());
	}

	@Test
	public void testFindAll() {
		ResponseEntity<List<Quote>> qr = restTemplate.exchange(BASE_URI + "/",
				HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Quote>>() {
				});

		assertNotNull("Should find a result.", qr);
		List<Quote> m = qr.getBody();
		assertNotNull(m);
		assertEquals(22, m.size());
	}
}
