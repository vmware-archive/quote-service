package org.springframework.nanotrader.quote;

import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import feign.Response;
import feign.gson.GsonDecoder;
import net.minidev.json.JSONArray;
import org.springframework.util.NumberUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuoteDecoder extends GsonDecoder {

    @Override
    public Object decode(Response response, Type type) throws IOException {

        Response.Body body = response.body();
        if (body == null) {
            return null;
        }

        // System.out.println(Util.toString(body.asReader()));

        Type typeOfListOfQuote = new TypeToken<List<Quote>>() {
        }.getType();
        if (Quote.class.equals(type) || typeOfListOfQuote.equals(type)) {
            return processBody(body);
        }

        return super.decode(response, type);
    }

    private float getFloat(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return 0;
        }
        return NumberUtils.parseNumber(o.toString(), Float.class);
    }

    private String getString(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    private int getInt(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return 0;
        }
        return NumberUtils.parseNumber(o.toString(), Integer.class);
    }

    private Object processBody(Response.Body body) throws IOException {
        ReadContext ctx = JsonPath.parse(body.asInputStream());
        try {
            ctx.read("$.symbol");
            return quoteFromJson(ctx);
        } catch (PathNotFoundException e) {
            return quotesFromJson(ctx);
        }
    }

    private Quote quoteFromJson(ReadContext ctx) {
        Quote q = new Quote();
        q.setCreated(getString(ctx, "$.latestTime"));
        q.setChange(getFloat(ctx, "$.change"));
        q.setName(getString(ctx, "$.companyName"));
        q.setPreviousClose((getFloat(ctx, "$.previousClose")));
        q.setPercentageChange(getString(ctx, "$.changePercent"));
        q.setAsk((getFloat(ctx, "$.iexAskPrice")));
        q.setBid((getFloat(ctx, "$.iexBidPrice")));
        q.setDaysHigh(getFloat(ctx, "$.high"));
        q.setDaysLow(getFloat(ctx, "$.low"));
        q.setOpen(getFloat(ctx, "$.open"));
        q.setPrice(getFloat(ctx, "$.latestPrice"));
        q.setSymbol(getString(ctx, "$.symbol"));
        q.setVolume(getInt(ctx, "$.latestVolume"));

        return q;
    }

    private List<Quote> quotesFromJson(ReadContext ctx) {
        ArrayList<Quote> quotes = new ArrayList<>();

        JSONArray ja = ctx.read("$.[*].quote");
        for (Object o : ja) {
            quotes.add(quoteFromJson(JsonPath.parse(o)));
        }

        return quotes;
    }

    static String formatSymbols(Set<String> symbols) {
        if (symbols == null || symbols.size() < 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String s : symbols) {
            sb.append(s);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}