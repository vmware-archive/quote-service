package org.springframework.nanotrader.quote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuoteRestTest {

	@LocalServerPort
	private int port;

	@Autowired
	QuoteController quoteController;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testFindBySymbol() {
		ResponseEntity<Quote> qr = restTemplate.getForEntity(
				getBaseUri() + "/PVTL", Quote.class);

		assertNotNull("Should find a result.", qr);
		Quote q = qr.getBody();
		assertNotNull(q);
		assertEquals("PVTL", q.getSymbol());
		assertEquals("Pivotal Inc.", q.getName());
	}

	@Test
	public void testMarketSummary() {
		ResponseEntity<Map<String, Object>> mr = restTemplate.exchange(getBaseUri()
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
		ResponseEntity<List<Quote>> qr = restTemplate.exchange(getBaseUri() + "/",
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
		ResponseEntity<Set<String>> qr = restTemplate.exchange(getBaseUri()
				+ "/symbols", HttpMethod.GET, null,
				new ParameterizedTypeReference<Set<String>>() {
				});

		assertNotNull("Should find a result.", qr);
		Set<String> s = qr.getBody();
		assertNotNull(s);
		assertEquals(22, s.size());
	}

	private String getBaseUri() {
		return "http://localhost:" + port + "/quotes";
	}
}
