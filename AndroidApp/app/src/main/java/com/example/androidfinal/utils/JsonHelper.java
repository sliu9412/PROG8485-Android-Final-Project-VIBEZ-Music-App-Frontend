package com.example.androidfinal.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Helper class for working with JSON data.
 */
public final class JsonHelper {

    private static final String TAG = JsonHelper.class.getSimpleName();

    public static final Gson GSON = new Gson();

    public static <T> T parse(String jsonObject, Class<T> classType) {
        try {
            return GSON.fromJson(jsonObject, classType);
        } catch (JsonSyntaxException err) {
            Log.e(TAG, "Error trying to parse '" + classType.getSimpleName() + "': " + jsonObject, err);
        }
        return null;
    }

    public static <T> String format(T object) {
        try {
            return GSON.toJson(object);
        } catch (JsonIOException err) {
            Log.e(TAG, "Error trying to format to json '" + object.getClass().getSimpleName() + "': " + object, err);
        }
        return null;
    }

    public static String getStrFromJsonElement(JsonElement element) {
        try {
            return element.getAsString();
        } catch (Exception err) {
            Log.e(TAG, "Error trying to get value from JsonElement" + element, err);
            return null;
        }
    }

    private JsonHelper() {
    }
}
