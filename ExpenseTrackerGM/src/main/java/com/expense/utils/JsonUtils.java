package com.expense.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtils {

	private static ObjectMapper jsonMapper = new ObjectMapper();
	static {
		JsonUtils.jsonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		JsonUtils.jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// jsonMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
	}

	public static String toJson(Object obj) {
		String json = "";
		try {
			json = JsonUtils.jsonMapper.writeValueAsString(obj);
			System.out.println("JSON = " + json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList toObjectList(String json, Class c) {
		ArrayList l = null;
		try {
			l = JsonUtils.jsonMapper.readValue(json,
					TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, c));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
}
