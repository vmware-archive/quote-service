package org.springframework.nanotrader.quote;

import java.util.Comparator;

public class AscendingChangeComparator implements Comparator<Quote> {

	@Override
	public int compare(Quote o1, Quote o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}

		if (o1 == null) {
			return -1;
		}

		if (o2 == null) {
			return 1;
		}

		if (o1.getChange() < o2.getChange()) {
			return -1;
		}

		if (o1.getChange() > o2.getChange()) {
			return 1;
		}

		return 0;
	}
}
