package org.anirudh.redquark.quarknews.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;

import org.anirudh.redquark.quarknews.model.NewsItem;

public class SharedPreference {

    private static final String PREFS_NAME = "NEWS_APP";
    private static final String FAVORITES = "NEWS_FAVORITE";

    public SharedPreference() {
        super();
    }

    // These four methods are used for maintaining favorites.
    private void saveFavorites(Context context, List<NewsItem> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public void addFavorite(Context context, NewsItem item) {
        List<NewsItem> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(item);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, NewsItem product) {
        ArrayList<NewsItem> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<NewsItem> getFavorites(Context context) {
        SharedPreferences settings;
        List<NewsItem> favorites;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            NewsItem[] favoriteItems = gson.fromJson(jsonFavorites,
                    NewsItem[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<NewsItem>) favorites;
    }
}