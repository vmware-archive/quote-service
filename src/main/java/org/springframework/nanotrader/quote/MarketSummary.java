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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

public class MarketSummary {

    private Float tradeStockIndexAverage;
    private Long tradeStockIndexVolume;
    private Float tradeStockIndexOpenAverage;
    private List<Quote> topLosers;
    private List<Quote> topGainers;
    private Date summaryDate;
    private Float change;
    private Float percentGain;

    public Float getChange() {
        return change;
    }

    public void setChange(Float change) {
        this.change = change;
    }

    public Float getPercentGain() {
        return percentGain;
    }

    public Float getTradeStockIndexAverage() {
        return tradeStockIndexAverage;
    }

    public void setTradeStockIndexAverage(Float tradeStockIndexAverage) {
        this.tradeStockIndexAverage = tradeStockIndexAverage;
    }

    public Long getTradeStockIndexVolume() {
        return tradeStockIndexVolume;
    }

    public void setTradeStockIndexVolume(Long tradeStockIndexVolume) {
        this.tradeStockIndexVolume = tradeStockIndexVolume;
    }

    public Float getTradeStockIndexOpenAverage() {
        return tradeStockIndexOpenAverage;
    }

    public void setTradeStockIndexOpenAverage(Float tradeStockIndexOpenAverage) {
        this.tradeStockIndexOpenAverage = tradeStockIndexOpenAverage;
    }

    public List<Quote> getTopLosers() {
        return topLosers;
    }

    public void setTopLosers(List<Quote> topLosers) {
        this.topLosers = topLosers;
    }

    public List<Quote> getTopGainers() {
        return topGainers;
    }

    public void setTopGainers(List<Quote> topGainers) {
        this.topGainers = topGainers;
    }

    public Date getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

    public void setPercentGain(Float percentGain) {
        this.percentGain = percentGain;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
