package org.springframework.nanotrader.quote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Symbols {

    private Set<String> symbols;

    @Autowired
    private QuoteController quoteController;

    Set<String> getSymbols() {
        if (symbols != null) {
            return symbols;
        }

        try {
            String jsonData = StreamUtils.copyToString(new ClassPathResource("symbols.json").getInputStream(), Charset.defaultCharset());
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<Quote> quotes = objectMapper.readValue(jsonData, new TypeReference<List<Quote>>() {
            });

            symbols = new HashSet<>();
            for (Quote quote : quotes) {
                symbols.add(quote.getSymbol());
            }

            return symbols;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean exists(String symbol) {
        if (symbol == null) {
            return false;
        }
        return getSymbols().contains(symbol);
    }

    int count() {
        return getSymbols().size();
    }

    Set<String> checkSymbols(Set<String> symbols) {
        Set<String> ret = new HashSet<>();
        if (symbols == null || symbols.size() < 1) {
            return ret;
        }
        for (String s : symbols) {
            if (exists(s)) {
                ret.add(s);
            }
        }
        return ret;
    }
}