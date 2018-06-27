package org.springframework.nanotrader.quote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuoteRepositoryPopulator implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (quoteRepository != null && quoteRepository.count() == 0) {
            populate();
        }
    }

    public void populate() {
        try {
            String jsonData = StreamUtils.copyToString(new ClassPathResource("quotes.json").getInputStream(), Charset.defaultCharset());
            ArrayList<Quote> q =  new ObjectMapper().readValue(jsonData, new TypeReference<List<Quote>>() {});
            quoteRepository.save(q);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}