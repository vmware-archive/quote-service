package org.springframework.nanotrader.quote;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minidev.json.JSONArray;

import org.springframework.util.NumberUtils;

import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.gson.GsonDecoder;

public class QuoteDecoder extends GsonDecoder {

	@Override
	public Object decode(Response response, Type type) throws IOException,
			DecodeException, FeignException {

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

	protected float getFloat(ReadContext ctx, String path) {
		Object o = ctx.read(path);
		if (o == null) {
			return 0;
		}
		return NumberUtils.parseNumber(o.toString(), Float.class);
	}

	protected String getString(ReadContext ctx, String path) {
		Object o = ctx.read(path);
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	protected int getInt(ReadContext ctx, String path) {
		Object o = ctx.read(path);
		if (o == null) {
			return 0;
		}
		return NumberUtils.parseNumber(o.toString(), Integer.class);
	}

	private Object processBody(Response.Body body) throws IOException {
		ReadContext ctx = JsonPath.parse(body.asInputStream());

		// is this a single quote, or a collection?
		int count = getInt(ctx, "$.query.count");
		if (count > 1) {
			return quotesFromJson(ctx);
		}

		return quoteFromJson(ctx);
	}

	private Quote quoteFromJson(ReadContext ctx) {
		Quote q = new Quote();
		q.setCreated(getString(ctx, "$.query.created"));
		q.setChange(getFloat(ctx, "$.query.results.quote.Change"));
		String name = ctx.read("$.query.results.quote.Name");
		if (name != null) {
			q.setName(name);
		}
		q.setPreviousClose((getFloat(ctx, "$.query.results.quote.PreviousClose")));
		q.setPercentageChange(getString(ctx, "$.query.results.quote.ChangeinPercent"));
		q.setAsk((getFloat(ctx, "$.query.results.quote.Ask")));
		q.setBid((getFloat(ctx, "$.query.results.quote.Bid")));
		q.setDaysHigh(getFloat(ctx, "$.query.results.quote.DaysHigh"));
		q.setDaysLow(getFloat(ctx, "$.query.results.quote.DaysLow"));
		q.setOpen(getFloat(ctx, "$.query.results.quote.PreviousClose"));
		q.setPrice(getFloat(ctx, "$.query.results.quote.LastTradePriceOnly"));
		q.setSymbol(getString(ctx, "$.query.results.quote.Symbol"));
		q.setVolume(getInt(ctx, "$.query.results.quote.Volume"));

		return q;
	}

	protected List<Quote> quotesFromJson(ReadContext ctx) {
		ArrayList<Quote> quotes = new ArrayList<Quote>();

		JSONArray qs = ctx.read("$.query.results.quote");
		for (int i = 0; i < qs.size(); i++) {
			Quote q = new Quote();
			q.setCreated(getString(ctx, "$.query.created"));
			q.setChange(getFloat(ctx, "$.query.results.quote[" + i + "].Change"));
			String name = ctx.read("$.query.results.quote[" + i + "].Name");
			if (name != null) {
				q.setName(name);
			}
			q.setPreviousClose((getFloat(ctx, "$.query.results.quote[" + i
					+ "].PreviousClose")));
			q.setPercentageChange(getString(ctx, "$.query.results.quote[" + i + "].ChangeinPercent"));
			q.setAsk((getFloat(ctx, "$.query.results.quote[" + i + "].Ask")));
			q.setBid((getFloat(ctx, "$.query.results.quote[" + i + "].Bid")));
			q.setDaysHigh(getFloat(ctx, "$.query.results.quote[" + i
					+ "].DaysHigh"));
			q.setDaysLow(getFloat(ctx, "$.query.results.quote[" + i
					+ "].DaysLow"));
			q.setOpen(getFloat(ctx, "$.query.results.quote[" + i
					+ "].PreviousClose"));
			q.setPrice(getFloat(ctx, "$.query.results.quote[" + i
					+ "].LastTradePriceOnly"));
			q.setSymbol(getString(ctx, "$.query.results.quote[" + i + "].Symbol"));
			q.setVolume(getInt(ctx, "$.query.results.quote[" + i + "].Volume"));
			quotes.add(q);
		}

		return quotes;
	}

	public static String formatSymbols(Set<String> symbols) {
		if (symbols == null || symbols.size() < 1) {
			return "()";
		}

		StringBuffer sb = new StringBuffer("(");
		for (String s : symbols) {
			sb.append("\"");
			sb.append(s);
			sb.append("\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}
}