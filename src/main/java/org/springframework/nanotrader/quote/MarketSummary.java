package org.springframework.nanotrader.quote;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MarketSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	private float average;
	private float open;
	private int volume;
	private float change;
	private float percentGain;

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
		setPercentGain(convertPercent(index.getPercentageChange()));
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

	public int getVolume() {
		return volume;
	}

	private void setVolume(int volume) {
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

	public float getPercentGain() {
		return percentGain;
	}

	private void setPercentGain(float percentGain) {
		this.percentGain = percentGain;
	}

	private float convertPercent(String percent) {
		if (percent == null || percent.trim().length() < 1) {
			return 0.0f;
		}

		String s = percent.trim();
		if (s.endsWith("%")) {
			s = s.substring(0, s.length() - 1);
		}

		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return 0.0f;
		}
	}
}
