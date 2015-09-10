package org.springframework.nanotrader.quote;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.util.NumberUtils;

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

		if (Quote.class.equals(type)) {
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

	private int getInt(ReadContext ctx, String path) {
		Object o = ctx.read(path);
		if (o == null) {
			return 0;
		}
		return NumberUtils.parseNumber(o.toString(), Integer.class);
	}

	private Object processBody(Response.Body body) throws IOException {
		return quoteFromJson(JsonPath.parse(body.asInputStream()));
	}

	private Quote quoteFromJson(ReadContext ctx) {
		Quote q = new Quote();
		q.setCreated(ctx.read("$.query.created").toString());
		q.setChange(getFloat(ctx, "$.query.results.quote.Change"));

		String name = ctx.read("$.query.results.quote.Name");
		if (name != null) {
			q.setName(name);
		}
		q.setPreviousClose((getFloat(ctx, "$.query.results.quote.PreviousClose")));

		String percentageChange = ctx
				.read("$.query.results.quote.ChangeinPercent");
		if (percentageChange != null) {
			q.setPercentageChange(percentageChange);
		}

		q.setAsk((getFloat(ctx, "$.query.results.quote.Ask")));
		q.setBid((getFloat(ctx, "$.query.results.quote.Bid")));
		q.setDaysHigh(getFloat(ctx, "$.query.results.quote.DaysHigh"));
		q.setDaysLow(getFloat(ctx, "$.query.results.quote.DaysLow"));
		q.setOpen(getFloat(ctx, "$.query.results.quote.PreviousClose"));
		q.setPrice(getFloat(ctx, "$.query.results.quote.LastTradePriceOnly"));
		q.setSymbol(ctx.read("$.query.results.quote.Symbol").toString());
		q.setVolume(getInt(ctx, "$.query.results.quote.Volume"));

		if (!hasValues(q)) {
			return null;
		}

		return q;
	}

	private boolean hasValues(Quote q) {
		if (q == null) {
			return false;
		}

		if (q.getName() == null && q.getPercentageChange() == null) {
			return false;
		}

		return true;
	}
}