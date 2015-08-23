package org.springframework.nanotrader.quote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class SymbolsTest {

	@Autowired
	Symbols symbols;

	@Test
	public void testSymbols() {
		assertEquals(23, symbols.count());
	}

	@Test
	public void testExists() {
		assertFalse(symbols.exists(null));
		assertFalse(symbols.exists(""));
		assertFalse(symbols.exists("Foo"));
		assertTrue(symbols.exists("GOOG"));
	}

	@Test
	public void testCheckSymbols() {
		Set<String> set = new HashSet<String>();
		Set<String> s1 = symbols.checkSymbols(set);
		assertNotNull(s1);
		assertEquals(0, s1.size());

		set.add("GOOG");
		s1 = symbols.checkSymbols(set);
		assertNotNull(s1);
		assertEquals(1, s1.size());
		assertTrue(s1.contains("GOOG"));

		set.add("YHOO");
		s1 = symbols.checkSymbols(set);
		assertNotNull(s1);
		assertEquals(2, s1.size());
		assertTrue(s1.contains("GOOG"));
		assertTrue(s1.contains("YHOO"));

		set.add("FOO");
		s1 = symbols.checkSymbols(set);
		assertNotNull(s1);
		assertEquals(2, s1.size());
		assertTrue(s1.contains("GOOG"));
		assertTrue(s1.contains("YHOO"));
		assertFalse(s1.contains("FOO"));
	}
}
