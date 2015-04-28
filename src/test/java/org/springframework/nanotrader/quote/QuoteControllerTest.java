/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * QuoteControllerTest tests the Quote REST api
 * 
 * @author Brian Dussault
 * @author
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class QuoteControllerTest {

	// Quote constants
	public static Integer QUOTE_ID = 95;
	public static String COMPANY_NAME = "VMware";
	public static BigDecimal HIGH = BigDecimal.valueOf(161.18);
	public static BigDecimal OPEN = BigDecimal.valueOf(159.18);
	public static BigDecimal VOLUME = new BigDecimal("0.00");
	public static BigDecimal CURRENT_PRICE = BigDecimal.valueOf(154.18);
	public static BigDecimal CHANGE_1 = new BigDecimal("3.00");
	public static BigDecimal LOW = BigDecimal.valueOf(145.18);
	public static String SYMBOL = "VMW";

	@Test
	public void getQuoteBySymbolJson() {
		ResponseEntity<Quote> qe = new TestRestTemplate().getForEntity(
				"http://localhost:8080/quote/VMW", Quote.class);
		assertTrue(qe.getStatusCode().equals(HttpStatus.OK));
		assertNotNull(qe);
		Quote quote = qe.getBody();
		assertNotNull(quote);
		assertEquals(CHANGE_1, quote.getChange1());
		assertEquals(COMPANY_NAME, quote.getCompanyname());
		assertEquals(HIGH, quote.getHigh());
		assertEquals(LOW, quote.getLow());
		assertEquals(OPEN, quote.getOpen1());
		assertEquals(CURRENT_PRICE, quote.getPrice());
		assertEquals(QUOTE_ID, quote.getQuoteid());
		assertEquals(SYMBOL, quote.getSymbol());
		assertEquals(VOLUME, quote.getVolume());

	}

	@Test
	public void getQuoteBySymbolNoRecordsFoundJson() {
		ResponseEntity<Quote> quote = new TestRestTemplate().getForEntity(
				"http://localhost:8080/quote/NOT_A_SYMBOL", Quote.class);
		assertTrue(quote.getStatusCode().equals(HttpStatus.NOT_FOUND));
		assertNull(quote.getBody());

	}

}
