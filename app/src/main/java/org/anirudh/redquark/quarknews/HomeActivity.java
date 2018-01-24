package org.anirudh.redquark.quarknews;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.anirudh.redquark.quarknews.adapter.CustomListViewAdapter;
import org.anirudh.redquark.quarknews.application.QuarkNewsApplication;
import org.anirudh.redquark.quarknews.asynctask.NewsContent;
import org.anirudh.redquark.quarknews.constants.Constant;
import org.anirudh.redquark.quarknews.model.NewsItem;
import org.anirudh.redquark.quarknews.receiver.ConnectivityReceiver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static org.anirudh.redquark.quarknews.constants.Constant.PORTFOLIO_URL;

public class HomeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    public static final String TAG = HomeActivity.class.getSimpleName();
    private ListView listView;
    private ArrayList<NewsItem> items;
    private ArrayAdapter<NewsItem> adapter;
    private Typeface typeface;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        NavigationView nvDrawer = findViewById(R.id.navView);
        setupDrawerContent(nvDrawer);

        typeface = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        listView = findViewById(R.id.list);
        setListViewAdapter();
        if(checkConnection()) {
            getData();
        }
    }

    private void getData(){
        new NewsContent(this, Constant.URL).execute();
    }

    private void setListViewAdapter(){
        items = new ArrayList<>();
        adapter = new CustomListViewAdapter(HomeActivity.this, this, R.layout.item_listview, items, typeface);
        listView.setAdapter(adapter);
    }

    // parse response data after async task finished
    public void parseJsonResponse(String result){
        Log.i(TAG, result);

        try{
            JSONArray jsonArray = new JSONArray(result);

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jObject = jsonArray.getJSONObject(i);

                /*Creating one news item at a time*/
                NewsItem newsItem = new NewsItem();

                newsItem.setId(jObject.getInt("ID"));
                newsItem.setTitle(jObject.getString("TITLE"));
                newsItem.setUrl(jObject.getString("URL"));
                newsItem.setPublisher(jObject.getString("PUBLISHER"));
                newsItem.setCategory(jObject.getString("CATEGORY"));
                newsItem.setHostname(jObject.getString("HOSTNAME"));
                newsItem.setTimestamp(jObject.getLong("TIMESTAMP"));

                items.add(newsItem);
            }

            // setting the adapter to show list
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Intent intent = new Intent(HomeActivity.this, WebActivity.class);
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_second_fragment:
                intent = new Intent(getApplicationContext(), PortfolioActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_third_fragment:
                intent.putExtra("loadUrl", PORTFOLIO_URL);
                intent.putExtra("title", "Profile");
                startActivity(intent);
                break;
            default:
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (!isConnected) {
            message = "Sorry! Not connected to internet. Please check your internet connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        QuarkNewsApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}