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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Quote implements Serializable {
    private static final long serialVersionUID = 1L;

    private String created;

    @JsonProperty("DaysLow")
    private float daysLow;

    @JsonProperty("Open")
    private float open;

    @JsonProperty("PreviousClose")
    private float previousClose;

    @JsonProperty("Volume")
    private int volume;

    @JsonProperty("Price")
    private float price;

    @JsonProperty("DaysHigh")
    private float daysHigh;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Symbol")
    @NotNull
    private String symbol = "";

    @JsonProperty("Change")
    private float change;

    @JsonProperty("PercentageChange")
    private String percentageChange;

    @JsonProperty("Ask")
    private float ask;

    @JsonProperty("Bid")
    private float bid;

    @Override
    public int hashCode() {
        return getSymbol().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quote)) {
            return false;
        }
        return o.hashCode() == hashCode();
    }
}