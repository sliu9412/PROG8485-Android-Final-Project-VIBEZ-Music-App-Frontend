package com.example.androidfinal.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PreferencesHelper {

    public static final String PREF_SPOTIFY = "spotify_data";
    public static final String KEY_SPOTIFY_TOKEN = "spotify_token";
    public static final String KEY_SPOTIFY_USER = "spotify_user";
    public static final String KEY_SPOTIFY_PLAYER_URI = "spotify_player_uri";
    public static final String KEY_REDIRECT_ACTIVITY = "post-spotify-auth-redirect";
    private static Gson GSON = new Gson();

    /**
     * This method allows to get a private shared preference instance.
     *
     * @param context  View Context Instance.
     * @param prefName Preference name.
     * @return SharedPreferences instance.
     */
    public static SharedPreferences getPrivatePreferences(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    /**
     * This method allows to put an entire object, using the key param, into a preference instance.
     *
     * @param preferences Shared Preferences instance.
     * @param key         Preference key.
     * @param value       Object to save in preferences.
     * @param <T>         Object Class Type.
     */
    public static <T> void put(SharedPreferences preferences, String key, T value) {
        final SharedPreferences.Editor preferencesEditor = preferences.edit();
        final String strJsonObject = GSON.toJson(value);
        preferencesEditor.putString(key, strJsonObject);
        preferencesEditor.commit();
    }

    /**
     * This method allows to get an object instance saved in preferences.
     *
     * @param preferences Shared Preferences instance.
     * @param key         Preference key.
     * @param classType   Object Class Type.
     * @param <T>         Object Class Type.
     * @return Object instance saved in preferences, if there is no key returns null.
     */
    public static <T> T get(SharedPreferences preferences, String key, Class<T> classType) {
        final String strJsonObject = preferences.getString(key, null);
        if (strJsonObject == null) {
            return null;
        }
        return GSON.fromJson(strJsonObject, classType);
    }

    /**
     * This method allows to delete an entire object, using the key param, into a preference instance.
     *
     * @param preferences Shared Preferences instance.
     * @param key         Preference key.
     */
    public static void delete(SharedPreferences preferences, String key) {
        final SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(key);
        preferencesEditor.commit();
    }

    /**
     * This method allows to clear all data saved in the preference instance.
     *
     * @param preferences Shared Preferences instance.
     */
    public static void clear(SharedPreferences preferences) {
        final SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.clear();
        preferencesEditor.commit();
    }

}

