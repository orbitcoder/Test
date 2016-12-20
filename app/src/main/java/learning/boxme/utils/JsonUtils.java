package learning.boxme.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class JsonUtils {

	private static Gson M_GSON = new Gson();

	public static String jsonify(Object object) {
		return M_GSON.toJson(object);
	}

	public static Object objectify(String jsonString, Class<?> T) {
		return M_GSON.fromJson(jsonString, T);
	}

	public static <T> Object arrayObjectify(String jsonString, Class<?> T) {
		return M_GSON.fromJson(jsonString, new TypeToken<T>() {
		}.getType());
	}
}
