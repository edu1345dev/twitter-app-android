package com.appbox.jose.twitterapp.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appbox.jose.twitterapp.Adapter.TweetsAdapter;
import com.appbox.jose.twitterapp.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.appbox.jose.twitterapp.Model.Tweeto;

import java.util.ArrayList;
import java.util.List;

public class ResultSearch extends AppCompatActivity implements SearchView.OnQueryTextListener {

    protected ListView tweetList;
    protected ArrayList<Tweeto> listTweeto = new ArrayList<>();
    protected ProgressBar progressBar;
    protected TextView tvNoResult;
    protected Button more;
    private String mQuery;
    private TweetsAdapter tweetsAdapter;
    private boolean loadingTweets = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        tweetList = (ListView) findViewById(R.id.lv_tweet_result);
        tvNoResult = (TextView) findViewById(R.id.tv_result_search_activity_no_results);

        progressBar = (ProgressBar) findViewById(R.id.progress_result_search);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary)
                ,PorterDuff.Mode.MULTIPLY);

        setupToolbar();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setToolbarTitle(query);
            mQuery = query;
            searchTweets(query);
        }
    }

    private void searchTweets(String query) {

        if (listTweeto.size() == 0){
            progressBar.setVisibility(View.VISIBLE);
            tweetList.setVisibility(View.GONE);
        }

            SearchTimeline searchTimeline = new SearchTimeline.Builder()
                    .maxItemsPerRequest(30)
                    .resultType(SearchTimeline.ResultType.MIXED)
                    .query(query)
                    .build();


            searchTimeline.next(listTweeto.size() != 0 ? listTweeto.get(listTweeto.size()-1).getId() : null,
                    new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result) {
                    if (result.data.items.size() != 0){
                        int lastListSize = listTweeto.size();

                        for (Tweet tweet : result.data.items) {
                            boolean adicionar = true;

                            Tweeto tweeto = new Tweeto(tweet.user.name,tweet.text,
                                    tweet.user.profileImageUrlHttps,tweet.id);

                            if (listTweeto.size() > 1){
                                for (Tweeto tweeto1 : listTweeto) {
                                    if (tweeto1.getId() == tweet.id){
                                        adicionar = false;
                                    }
                                }
                            }

                            if (adicionar){
                                listTweeto.add(tweeto);
                            }
                        }

                        if (listTweeto.size() > lastListSize){
                            loadingTweets = false;
                        }

                        populateList(listTweeto);
                    }else {
                        if (listTweeto.size() == 0){
                            showNoResultView();
                        }
                    }
                }

                @Override
                public void failure(TwitterException exception) {
                    loadingTweets = false;
                }
            });
        }

    private void showNoResultView() {
        progressBar.setVisibility(View.GONE);
        tweetList.setVisibility(View.GONE);
        tvNoResult.setVisibility(View.VISIBLE);
    }

    private void populateList(ArrayList<Tweeto> listTweeto) {

        progressBar.setVisibility(View.GONE);
        tweetList.setVisibility(View.VISIBLE);

        if (tweetList.getAdapter() == null){
            tweetsAdapter = new TweetsAdapter(this,listTweeto);
            tweetList.setAdapter(tweetsAdapter);
        }else {
            tweetsAdapter.notifyDataSetChanged();
        }

        tweetList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //faz nada
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem+visibleItemCount == totalItemCount){
                    if (!loadingTweets){
                        searchTweets(mQuery);
                        loadingTweets = true;
                    }
                }
            }
        });

    }

    private void setToolbarTitle(String query) {

        try {
            getSupportActionBar().setTitle(getString(R.string.text_search)+" "+query);
        }catch (Exception e){
            Log.d("Exception",e.toString());
        }

    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);

        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            setSupportActionBar(toolbar);
            try {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }catch (Exception e){
                Log.d("Exception",e.toString());
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
