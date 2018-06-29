package org.springframework.nanotrader.quote;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    public static final String INDEX_SYMBOL = "IDX";

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private Symbols symbols;

    @Autowired
    private CacheManager manager;

    @Cacheable(value = "quotes", unless = "#result == null")
    @RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
    public Quote findBySymbol(@PathVariable String symbol) {
        if (symbol == null || !symbols.exists(symbol)) {
            return null;
        }
        return getQuote(symbol);
    }

    private long countAllQuotes() {
        return symbols.getSymbols().size();
    }

    private Quote getIndexInfo() {
        DecimalFormat df = new DecimalFormat("#.##");

        //data provider does not support index, so we are creating our own simple one
        List<Quote> quotes = findAll();
        float open = 0;
        float previousClose = 0;
        float price = 0;
        float volume = 0;

        for (Quote q : quotes) {
            open += q.getOpen();
            previousClose += q.getPreviousClose();
            price += q.getPrice();
            volume += q.getVolume();
        }

        //todo, store day high, low, for comparisons.
        //for now just average everything
        Quote q = new Quote();
        q.setName("Index");
        q.setOpen(Float.valueOf(df.format(open / quotes.size())));
        q.setPreviousClose(Float.valueOf(df.format(previousClose / quotes.size())));
        q.setPrice(Float.valueOf(df.format(price / quotes.size())));
        q.setChange(q.getPrice() - q.getOpen());

        if (q.getChange() > 0) {
            q.setPercentageChange("" + Float.valueOf(df.format(100 * (q.getChange() / q.getOpen()))));
        } else {
            q.setPercentageChange("" + Float.valueOf(df.format(100 * ((q.getOpen() - q.getPrice()) / q.getOpen()))));
        }

        q.setPercentageChange("" + Float.valueOf(df.format(100 * (q.getChange() / q.getOpen()))));
        q.setCreated("" + System.currentTimeMillis());
        q.setSymbol("IDX");
        q.setVolume(Math.round(volume / quotes.size()));
        return q;
    }

    private Quote getQuote(String symbol) {
        return quoteRepository
                .getQuote(symbol);
    }

    @RequestMapping("/topGainers")
    public List<Quote> topGainers() {
        List<Quote> l = new ArrayList<Quote>(findAll());
        Collections.sort(l, new DescendingChangeComparator());
        return l.subList(0, 3);
    }

    @RequestMapping("/topLosers")
    public List<Quote> topLosers() {
        List<Quote> l = new ArrayList<Quote>(findAll());
        Collections.sort(l, new AscendingChangeComparator());
        return l.subList(0, 3);
    }

    @Cacheable(value = "index", key = "'marketSummary'")
    @RequestMapping("/marketSummary")
    public MarketSummary marketSummary() {
        return new MarketSummary(getIndexInfo());
    }

    @RequestMapping("/")
    public List<Quote> findAll() {

        // check to see if we have all of our symbols in the cache
        Cache cache = manager.getCache("quotes");
        cache.evictExpiredElements();

        if (cache.getKeys().size() < countAllQuotes()) {
            // some are not in cache, clear things out and go get all of them.
            cache.removeAll();

            String query = QuoteDecoder.formatSymbols(symbols.getSymbols());
            List<Quote> quotes = quoteRepository.getQuotes(query);

            // load these into the cache
            for (Quote q : quotes) {
                cache.put(new Element(q.getSymbol(), q));
            }
            return quotes;
        }

        // otherwise cache has everything, just return cached values
        List<Quote> l = new ArrayList<Quote>();

        @SuppressWarnings("rawtypes")
        List keys = cache.getKeys();
        for (Object key : keys) {
            Object o = cache.get(key).getObjectValue();
            if (o instanceof Quote) {
                l.add((Quote) o);
            }
        }
        return l;
    }

    @RequestMapping("/symbols")
    public Set<String> symbols() {
        return symbols.getSymbols();
    }

}