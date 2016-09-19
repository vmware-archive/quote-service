package org.springframework.nanotrader.quote;

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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@WebIntegrationTest(value = "server.port=9876")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class QuoteRestTest {

    private static final String BASE_URI = "http://localhost:9876/quotes";

    @Autowired
    private QuoteController quoteController;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testFindBySymbol() {
        ResponseEntity<Quote> qr = restTemplate.getForEntity(BASE_URI
                + "/findBySymbol/GOOG", Quote.class);

        assertNotNull("Should find a result.", qr);
        Quote q = qr.getBody();
        assertNotNull(q);
        assertEquals("GOOG", q.getSymbol());
        assertEquals("Google Inc.", q.getCompanyname());
    }

    @Test
    public void testMarketSummary() {
        ResponseEntity<MarketSummary> ms = restTemplate.exchange(BASE_URI
                        + "/marketSummary", HttpMethod.GET, null,
                new ParameterizedTypeReference<MarketSummary>() {
                });

        assertNotNull("Should find a result.", ms);
    }

    @Test
    public void testFindAll() {
        ResponseEntity<List<Quote>> qr = restTemplate.exchange(BASE_URI
                        + "/findAll", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Quote>>() {
                });

        assertNotNull("Should find a result.", qr);
        List<Quote> m = qr.getBody();
        assertNotNull(m);
        assertEquals(22, m.size());
    }
}
