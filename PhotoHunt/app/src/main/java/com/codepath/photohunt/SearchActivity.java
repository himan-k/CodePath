package com.codepath.photohunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private GridView gdResults;
    private static final String LOG_TAG = "JSONStreamReader";
    private PhotoAdapter aPhotos;
    private ArrayList<Photo> photos = new ArrayList<Photo>();
    private static String currentQuery = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // find views in the application
        setupViews();
        // Set a ToolBar to replace the ActionBar.
        setSupportActionBar(toolbar);
        aPhotos = new PhotoAdapter(this, photos);
        gdResults.setAdapter(aPhotos);

        gdResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchResults(currentQuery, false);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        gdResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageViewActivity.class);

                Photo photo = photos.get(position);
                i.putExtra("url", photo.getImageUrl());
                startActivity(i);
            }
        });
    }

    private void setupViews() {
        // get gridview
        gdResults = (GridView) findViewById(R.id.gvResults);
        // get toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                // Fetch the data remotely
                fetchResults(query, false);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery(query, false);
                searchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void fetchResults(String query, final boolean addFront) {
        //URI to get the JSON stream data array of countries
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + query;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONObject("responseData").getJSONArray("results");
                    Log.i("DEBUG Json array length:", Integer.toString(photosJSON.length()));
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        Photo photo = new Photo();

                        photo.setImageUrl(photoJSON.getString("url"));
                        if (addFront)
                            photos.add(0, photo);
                        else
                            photos.add(photo);
                        //swipeContainer.setRefreshing(false);
                    }
                    //Log.i("DEBUG", response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchView search = (SearchView) MenuItemCompat.getActionView(item);
                //search.setQuery("Himanshu Kale", false);
                //Toast.makeText(this, "Selected: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

