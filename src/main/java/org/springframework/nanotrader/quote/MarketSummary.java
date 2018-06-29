package org.springframework.nanotrader.quote;

import lombok.Data;

import java.io.Serializable;

@Data
class MarketSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private float average;
    private float open;
    private int volume;
    private float change;
    private float percentGain;

    MarketSummary(Quote index) {
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
