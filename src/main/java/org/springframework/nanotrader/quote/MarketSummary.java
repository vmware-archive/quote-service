package org.springframework.nanotrader.quote;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MarketSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	private float average;
	private float open;
	private float volume;
	private float change;
	private List<Quote> topLosers;
	private List<Quote> topGainers;

	public MarketSummary(Quote index, List<Quote> topLosers,
			List<Quote> topGainers) {
		load(index, topLosers, topGainers);
	}

	private void load(Quote index, List<Quote> topLosers, List<Quote> topGainers) {
		if (index == null || topLosers == null || topGainers == null) {
			return;
		}
		setAverage(index.getPrice());
		setOpen(index.getOpen());
		setVolume(index.getVolume());
		setChange(index.getChange());
		setTopLosers(topLosers);
		setTopGainers(topGainers);
	}

	public float getAverage() {
		return average;
	}

	private void setAverage(float average) {
		this.average = average;
	}

	public float getOpen() {
		return open;
	}

	private void setOpen(float open) {
		this.open = open;
	}

	public float getVolume() {
		return volume;
	}

	private void setVolume(float volume) {
		this.volume = volume;
	}

	public float getChange() {
		return change;
	}

	private void setChange(float change) {
		this.change = change;
	}

	public List<Quote> getTopLosers() {
		return topLosers;
	}

	private void setTopLosers(List<Quote> topLosers) {
		this.topLosers = topLosers;
	}

	public List<Quote> getTopGainers() {
		return topGainers;
	}

	private void setTopGainers(List<Quote> topGainers) {
		this.topGainers = topGainers;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
