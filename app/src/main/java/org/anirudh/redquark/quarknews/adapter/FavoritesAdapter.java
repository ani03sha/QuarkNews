package org.anirudh.redquark.quarknews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.anirudh.redquark.quarknews.HomeActivity;
import org.anirudh.redquark.quarknews.R;
import org.anirudh.redquark.quarknews.WebActivity;
import org.anirudh.redquark.quarknews.model.NewsItem;
import org.anirudh.redquark.quarknews.util.SharedPreference;

import java.util.List;

public class FavoritesAdapter extends ArrayAdapter<NewsItem> {
    private Activity activity;
    private Typeface typeface;
    private Context context;
    private SharedPreference sharedPreference;
    private List<NewsItem> newsItems;

    public FavoritesAdapter(Context context, Activity activity, int resource, List<NewsItem> items, Typeface typeface) {
        super(activity, resource, items);
        this.activity = activity;
        this.typeface = typeface;
        this.newsItems = items;
        this.context = context;
        sharedPreference = new SharedPreference();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

        FavoritesAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if(convertView == null){
            // inflate UI from the XML file
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.favorites_listview, parent, false);
            }
            //get all UI view
            holder = new FavoritesAdapter.ViewHolder(convertView);
            // set Tag for holder
            if (convertView != null) {
                convertView.setTag(holder);
            }
        }else{
            // if holder created, get tag from view
            holder = (FavoritesAdapter.ViewHolder) convertView.getTag();
        }

        final NewsItem item = getItem(position);
        if (item != null) {
            holder.title.setText(item.getTitle());
            holder.publisher.setText(String.format("From: %s", item.getPublisher()));
            String businessCategory = item.getCategory();

            switch (businessCategory){

                case "b":
                    businessCategory = "Business";
                    break;

                case "t":
                    businessCategory = "Science & Technology";
                    break;

                case "e":
                    businessCategory = "Entertainment";
                    break;

                case "m":
                    businessCategory = "Health";
            }
            holder.category.setText(String.format("Category: %s", businessCategory));
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, WebActivity.class);
                    intent.putExtra("loadUrl", item.getUrl());
                    activity.startActivity(intent);
                }
            });

            holder.viewStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, WebActivity.class);
                    intent.putExtra("loadUrl", item.getUrl());
                    activity.startActivity(intent);
                }
            });

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = checkFavoriteItem(item);
                    if(!flag){
                        sharedPreference.removeFavorite(activity, item);
                    }
                    Toast.makeText(activity, "Item is removed from Favorites", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, HomeActivity.class));
                }
            });
        }

        /*If a product exists in shared preferences then set heart_red drawable
         * and set a tag*/
        if (checkFavoriteItem(item)) {
            holder.remove.setTypeface(typeface);
            holder.remove.setTag("grey");
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView title, category;
        private TextView publisher;
        private Button remove, viewStory;

        ViewHolder(View v) {
            title = v.findViewById(R.id.title);
            publisher = v.findViewById(R.id.publisher);
            remove = v.findViewById(R.id.remove);
            viewStory = v.findViewById(R.id.viewStory);
            category = v.findViewById(R.id.category);
        }
    }

    /*Checks whether a particular product exists in SharedPreferences*/
    private boolean checkFavoriteItem(NewsItem newsItem) {
        boolean check = false;
        List<NewsItem> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (NewsItem product : favorites) {
                if (product.equals(newsItem)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(NewsItem newsItem) {
        super.add(newsItem);
        newsItems.add(newsItem);
        notifyDataSetChanged();
    }

    @Override
    public void remove(NewsItem newsItem) {
        super.remove(newsItem);
        newsItems.remove(newsItem);
        notifyDataSetChanged();
    }
}
