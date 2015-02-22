package com.codepath.apps.tweeter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.tweeter.adapters.TweetListAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;
    private TweeterClient client;
    private TweetListAdapter tweetsAdapter;
    private SwipeRefreshLayout swipeContainer;
    private Toolbar toolbar;

    private boolean isFetchingTweets = false;
    private boolean listIsExhausted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // get toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set a ToolBar to replace the ActionBar.
        setSupportActionBar(toolbar);
        //initialize tweet list
        tweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetListAdapter(this, tweets);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);

        client = TweeterApplication.getRestClient();
        fetchHomeTweets(TweeterClient.METHOD_HOME_TIMELINE, null);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchHomeTweets(TweeterClient.METHOD_HOME_TIMELINE, null);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchHomeTweets(TweeterClient.METHOD_HOME_TIMELINE, null);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void fetchHomeTweets(String method, Long userId) {
        if (isFetchingTweets) return;
        isFetchingTweets = true;

        Tweet lastTweet = null;
        int numberOfTweets = tweets.size();
        if (numberOfTweets > 0)
            lastTweet = tweets.get(tweets.size() - 1);

        Long oldestId = (lastTweet != null) ? lastTweet.getTweetId() - 1 : null;

        client.getStatuses(method, userId, oldestId, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                endLoading();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONArray response) {
                ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                tweetsAdapter.addAll(newTweets);
                listIsExhausted = newTweets.size() == 0;

                endLoading();
            }

            private void endLoading() {
                isFetchingTweets = false;
                tweetsAdapter.notifyDataSetChanged();
            }
        });
    }
}
