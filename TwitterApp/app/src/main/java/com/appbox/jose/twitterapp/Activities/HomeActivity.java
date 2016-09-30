package com.appbox.jose.twitterapp.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbox.jose.twitterapp.Adapter.SearchTermAdapter;
import com.appbox.jose.twitterapp.Interface.SearchTermClickInterface;
import com.appbox.jose.twitterapp.Model.RecentSearch;
import com.appbox.jose.twitterapp.R;
import com.google.gson.Gson;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

/**
 * Created by edu_1 on 28/09/2016.
 */
public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchTermClickInterface {

    protected ListView lvRecentSearch;
    protected Set<String> recentSearchSet = new HashSet<>();
    protected SearchView searchView;
    protected MenuItem menuItem;
    protected TextView tvNoSearch;
    protected TextView tvRecentSearch;
    protected SharedPreferences preferences;
    protected SearchTermAdapter searchTermAdapter;
    protected View viewDivisor;

    private static final String TWITTER_KEY = "YFUEvPZyQU6S0YmUPVVUQ8xrV";
    private static final String TWITTER_SECRET = "JqQA94wkZjapqBKa4Z9YNKAgC2L4Cw3ppeu10AuIESfNznb6TE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        preferences = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

        setContentView(R.layout.home_activity);
        tvNoSearch = (TextView) findViewById(R.id.tv_home_activity_nosearch);
        lvRecentSearch = (ListView) findViewById(R.id.lv_home_activity_recent_search);
        tvRecentSearch = (TextView) findViewById(R.id.tv_home_activity_recent_search);
        viewDivisor = findViewById(R.id.view_home_activity_divisor);

    }

    private void populateList(ArrayList<String> recentSearch) {

        Log.d("populate",recentSearch.toString());
        searchTermAdapter = new SearchTermAdapter(this,recentSearch,this);
        lvRecentSearch.setAdapter(searchTermAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();

        Gson gson = new Gson();
        String string = preferences.getString(getResources().getString(R.string.recent_search),"");
        RecentSearch recentSearch = gson.fromJson(string,RecentSearch.class);

        if (string.equals("") || recentSearch.getSearchList().size() == 0){
            tvNoSearch.setVisibility(View.VISIBLE);
            tvRecentSearch.setVisibility(View.GONE);
            viewDivisor.setVisibility(View.GONE);
            RecentSearch recent = new RecentSearch();
            preferences
                    .edit()
                    .putString(getResources().getString(R.string.recent_search), recent.getGson())
                    .apply();
        }else {
            tvNoSearch.setVisibility(View.GONE);
            tvRecentSearch.setVisibility(View.VISIBLE);
            viewDivisor.setVisibility(View.VISIBLE);
            ArrayList<String> list = getRecentSearch().getSearchList();
            populateList(list);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_action_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.text_delete_history))
                    .setPositiveButton(getString(R.string.text_confirmation), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           deletarHistorico();
                        }
                    })
                    .setNegativeButton(getString(R.string.text_negation), null);

            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);

    }

    private void deletarHistorico() {
        Gson gson = new Gson();
        String string = preferences.getString(getResources().getString(R.string.recent_search),"");
        RecentSearch recentSearch = gson.fromJson(string,RecentSearch.class);
        List<String> list = recentSearch.getSearchList();
        list.clear();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.recent_search),recentSearch.getGson());
        editor.apply();

        ArrayList<String> listAux = getRecentSearch().getSearchList();
        populateList(listAux);
        onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        menuItem = menu.findItem(R.id.home_action_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) menuItem.getActionView();
        } else {
            searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        }

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.home_search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Gson gson = new Gson();
        String string = preferences.getString(getResources().getString(R.string.recent_search),"");
        RecentSearch recentSearch = gson.fromJson(string,RecentSearch.class);

        List<String> list = recentSearch.getSearchList();

        if (list.size() == 0){
            list.add(query);
        }else list.add(0,query);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.recent_search),recentSearch.getGson());
        editor.apply();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public RecentSearch getRecentSearch(){
        Gson gson = new Gson();
        String string = preferences.getString(getResources().getString(R.string.recent_search),"");
        RecentSearch recentSearch = gson.fromJson(string,RecentSearch.class);
        return recentSearch;
    }

    @Override
    public void onSearchItemClick(String search) {
        searchView.setQuery(search,true);
    }
}
