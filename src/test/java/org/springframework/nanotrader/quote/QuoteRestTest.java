package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
		assertEquals("Alphabet Inc.", q.getName());
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
		assertEquals(5, m.size());
		assertNotNull(m.get("average"));
		assertNotNull(m.get("open"));
		assertNotNull(m.get("volume"));
		assertNotNull(m.get("change"));
		assertNotNull(m.get("percentGain"));
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

	@Test
	public void testSymbols() {
		ResponseEntity<Set<String>> qr = restTemplate.exchange(BASE_URI
				+ "/symbols", HttpMethod.GET, null,
				new ParameterizedTypeReference<Set<String>>() {
				});

		assertNotNull("Should find a result.", qr);
		Set<String> s = qr.getBody();
		assertNotNull(s);
		assertEquals(22, s.size());
	}
}
