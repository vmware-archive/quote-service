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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuoteRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private QuoteController quoteController;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testFindBySymbol() {
        ResponseEntity<Quote> qr = restTemplate.getForEntity(getBaseUri() + "/findBySymbol/GOOG", Quote.class);

        assertNotNull("Should find a result.", qr);
        Quote q = qr.getBody();
        assertNotNull(q);
        assertEquals("GOOG", q.getSymbol());
        assertEquals("Google Inc.", q.getCompanyname());
    }

    @Test
    public void testMarketSummary() {
        ResponseEntity<MarketSummary> ms = restTemplate.exchange(getBaseUri() + "/marketSummary", HttpMethod.GET, null,
                new ParameterizedTypeReference<MarketSummary>() {
                });

        assertNotNull("Should find a result.", ms);
    }

    @Test
    public void testFindAll() {
        ResponseEntity<List<Quote>> qr = restTemplate.exchange(getBaseUri() + "/findAll", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Quote>>() {
                });

        assertNotNull("Should find a result.", qr);
        List<Quote> m = qr.getBody();
        assertNotNull(m);
        assertEquals(22, m.size());
    }

    private String getBaseUri() {
        return "http://localhost:" + port + "/quotes";
    }
}
