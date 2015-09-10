package org.springframework.nanotrader.quote;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MarketSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	private float average;
	private float open;
	private float volume;
	private float change;
	private String percentGain;

	public MarketSummary(Quote index) {
		load(index);
	}

	private void load(Quote index) {
		if (index == null) {
			return;
		}
		setAverage(index.getPrice());
		setOpen(index.getOpen());
		setVolume(index.getVolume());
		setChange(index.getChange());
		setPercentGain(index.getPercentageChange());
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

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getPercentGain() {
		return percentGain;
	}

	private void setPercentGain(String percentGain) {
		this.percentGain = percentGain;
	}
}
