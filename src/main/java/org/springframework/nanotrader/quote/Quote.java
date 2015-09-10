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

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote implements Serializable {
	private static final long serialVersionUID = 1L;

	private String created;
	private float daysLow;
	private float open;
	private float previousClose;
	private int volume;
	private float price;
	private float daysHigh;
	private String name;
	private String symbol = "";
	private float change;
	private String percentageChange;
	private float ask;
	private float bid;

	@Override
	public int hashCode() {
		return getSymbol().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Quote)) {
			return false;
		}
		return o.hashCode() == hashCode();
	}

	@JsonProperty("DaysLow")
	public float getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(float f) {
		this.daysLow = f;
	}

	@JsonProperty("Open")
	public float getOpen() {
		return open;
	}

	public void setOpen(float f) {
		this.open = f;
	}

	@JsonProperty("Volume")
	public int getVolume() {
		return volume;
	}

	public void setVolume(int i) {
		this.volume = i;
	}

	@JsonProperty("Price")
	public float getPrice() {
		return price;
	}

	public void setPrice(float f) {
		this.price = f;
	}

	@JsonProperty("DaysHigh")
	public float getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(float f) {
		this.daysHigh = f;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}

	@JsonProperty("Symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String s) {
		if (s != null) {
			this.symbol = s;
		}
	}

	@JsonProperty("Change")
	public float getChange() {
		return change;
	}

	public void setChange(float f) {
		this.change = f;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String s) {
		this.created = s;
	}

	@JsonProperty("PreviousClose")
	public float getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(float f) {
		this.previousClose = f;
	}

	@JsonProperty("PercentageChange")
	public String getPercentageChange() {
		return percentageChange;
	}

	public void setPercentageChange(String s) {
		this.percentageChange = s;
	}

	@JsonProperty("Ask")
	public float getAsk() {
		return ask;
	}

	public void setAsk(float f) {
		this.ask = f;
	}

	@JsonProperty("Bid")
	public float getBid() {
		return bid;
	}

	public void setBid(float f) {
		this.bid = f;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}