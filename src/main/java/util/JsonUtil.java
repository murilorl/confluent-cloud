package util;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

public class JsonUtil {

	static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static String toJsonString(Object src) {
		return gson.toJson(src);
	}
}