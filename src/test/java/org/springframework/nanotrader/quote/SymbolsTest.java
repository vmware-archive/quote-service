package org.springframework.nanotrader.quote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SymbolsTest {

	@Autowired
	Symbols symbols;

	@Test
	public void testSymbols() {
		assertEquals(22, symbols.count());
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
