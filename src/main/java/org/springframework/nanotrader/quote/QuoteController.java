package org.springframework.nanotrader.quote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/quotes")
public class QuoteController implements QuoteService {

    @Autowired
    QuoteRepository quoteRepository;

    @RequestMapping("/count")
    public long countAllQuotes() {
        return quoteRepository.count();
    }

    @RequestMapping("/findAll")
    public List<Quote> findAllQuotes() {
        List<Quote> q = new ArrayList<Quote>();
        Iterator<Quote> i = quoteRepository.findAll().iterator();
        while (i.hasNext()) {
            q.add(i.next());
        }
        return q;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Quote saveQuote(@RequestBody Quote quote) {
        return quoteRepository.save(quote);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteQuote(@RequestBody Quote quote) {
        quoteRepository.delete(quote);
    }

    @RequestMapping("/findBySymbol/{symbol}")
    public Quote findBySymbol(@PathVariable String symbol) {
        return quoteRepository.findBySymbol(symbol);
    }

    @RequestMapping("/findBySymbolIn")
    public List<Quote> findBySymbolIn(@RequestParam Set<String> symbols) {
        return quoteRepository.findBySymbolIn(symbols);
    }

    private Float indexAverage() {
        return quoteRepository.findIndexAverage();
    }

    private Float openAverage() {
        return quoteRepository.findOpenAverage();
    }

    private float percentGain() {
        return (indexAverage() / openAverage()) * 100;
    }

    private Long volume() {
        return quoteRepository.findVolume();
    }

    private Float change() {
        return indexAverage() - openAverage();
    }

    @RequestMapping("/topGainers")
    public List<Quote> topGainers() {
        return quoteRepository
                .findAllByOrderByChangeDesc(new PageRequest(0, 3));
    }

    @RequestMapping("/topLosers")
    public List<Quote> topLosers() {
        return quoteRepository.findAllByOrderByChangeAsc(new PageRequest(0, 3));
    }

    @RequestMapping("/marketSummary")
    public MarketSummary marketSummary() {
        MarketSummary ms = new MarketSummary();
        ms.setChange(change());
        ms.setPercentGain(percentGain());
        ms.setSummaryDate(new Date());
        ms.setTopGainers(topGainers());
        ms.setTopLosers(topLosers());
        ms.setTradeStockIndexAverage(indexAverage());
        ms.setTradeStockIndexOpenAverage(openAverage());
        ms.setTradeStockIndexVolume(volume());

        return ms;
    }
}
