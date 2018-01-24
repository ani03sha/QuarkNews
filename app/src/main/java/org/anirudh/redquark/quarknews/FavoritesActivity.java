package org.anirudh.redquark.quarknews;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.anirudh.redquark.quarknews.adapter.FavoritesAdapter;
import org.anirudh.redquark.quarknews.application.QuarkNewsApplication;
import org.anirudh.redquark.quarknews.model.NewsItem;
import org.anirudh.redquark.quarknews.receiver.ConnectivityReceiver;
import org.anirudh.redquark.quarknews.util.SharedPreference;

import java.util.List;

import static org.anirudh.redquark.quarknews.constants.Constant.PORTFOLIO_URL;


public class FavoritesActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private SharedPreference sharedPreference;
    private List<NewsItem> items;
    private FavoritesAdapter adapter;
    private Typeface typeface;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        sharedPreference = new SharedPreference();
        items = sharedPreference.getFavorites(this);

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


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        if(items == null){
            showAlert(getResources().getString(R.string.no_favorites_items),
                    getResources().getString(R.string.no_favorites_msg));
        }else{

            if (items.size() == 0){
                showAlert(getResources().getString(R.string.no_favorites_items), getResources().getString(R.string.no_favorites_msg));
            }

            ListView favoriteList = findViewById(R.id.list);

            if(items != null){
                adapter = new FavoritesAdapter(FavoritesActivity.this, this, R.layout.item_listview, items, typeface);
                favoriteList.setAdapter(adapter);

                favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });

                favoriteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (checkConnection()) {

                            Button button = findViewById(R.id.favorite);
                            String tag = button.getTag().toString();

                            if (!tag.equalsIgnoreCase("red")) {
                                sharedPreference.addFavorite(FavoritesActivity.this, items.get(i));
                                Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
                                button.setTag("red");
                            } else {
                                sharedPreference.removeFavorite(FavoritesActivity.this, items.get(i));
                                button.setTag("grey");
                                adapter.remove(items.get(i));
                                Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                });
            }
        }
    }

    public void showAlert(String title, String message) {
        if (!FavoritesActivity.this.isFinishing()) {
            AlertDialog alertDialog = new AlertDialog.Builder(FavoritesActivity.this).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);

            // setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getFragmentManager().popBackStackImmediate();
                            startActivity(new Intent(FavoritesActivity.this, HomeActivity.class));
                        }
                    });
            alertDialog.show();
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
        Intent intent = new Intent(FavoritesActivity.this, WebActivity.class);
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                intent = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_second_fragment:
                intent = new Intent(FavoritesActivity.this, SettingsActivity.class);
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
