package com.example.itunessearcher.model;

import androidx.arch.core.util.Function;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class BestNumberTypeAdapterFactory
		implements TypeAdapterFactory {

	private static final List<Function<? super String, ? extends Number>> parsers = getParsers();

	private static final TypeAdapterFactory bestNumberTypeAdapterFactory = new BestNumberTypeAdapterFactory();

	private static final TypeToken<Number> numberTypeToken = new TypeToken<Number>() {
	};

	private BestNumberTypeAdapterFactory() {
	}

	static TypeAdapterFactory getBestNumberTypeAdapterFactory() {
		return bestNumberTypeAdapterFactory;
	}

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
		if ( Number.class.isAssignableFrom(typeToken.getRawType()) ) {
			final TypeAdapter<Number> delegateNumberTypeAdapter = gson.getDelegateAdapter(this, numberTypeToken);
			final TypeAdapter<Number> numberTypeAdapter = new NumberTypeAdapter(delegateNumberTypeAdapter).nullSafe();
			@SuppressWarnings("unchecked")
			final TypeAdapter<T> typeAdapter = (TypeAdapter<T>) numberTypeAdapter;
			return typeAdapter;
		}
		return null;
	}

	private static final class NumberTypeAdapter
			extends TypeAdapter<Number> {

		private final TypeAdapter<Number> delegateNumberTypeAdapter;

		private NumberTypeAdapter(final TypeAdapter<Number> delegateNumberTypeAdapter) {
			this.delegateNumberTypeAdapter = delegateNumberTypeAdapter;
		}

		@Override
		public void write(final JsonWriter out, final Number value)
				throws IOException {
			delegateNumberTypeAdapter.write(out, value);
		}

		@Override
		public Number read(final JsonReader in)
				throws IOException {
			final String s = in.nextString();
			return parsers.stream()
					.map(parser -> parser.apply(s))
					.filter(Objects::nonNull)
					.findFirst()
					.get();
		}
	}

	private static <N extends Number> Function<String, N> parseOnNull(final Function<? super String, ? extends N> parser) {
		return s -> {
			try {
				return parser.apply(s);
			} catch ( final NumberFormatException ignored ) {
				return null;
			}
		};
	}

	private static final List<Function<? super String, ? extends Number>> getParsers() {
		List<Function<? super String, ? extends Number>> parsersList = new ArrayList<>();

		parsersList.add(parseOnNull(Byte::parseByte));
		parsersList.add(parseOnNull(Short::parseShort));
		parsersList.add(parseOnNull(Integer::parseInt));
		parsersList.add(parseOnNull(Long::parseLong));
		parsersList.add(parseOnNull(Float::parseFloat));
		parsersList.add(parseOnNull(Double::parseDouble));
		return parsersList;
	}

}