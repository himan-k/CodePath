package com.codepath.photohunt.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.codepath.photohunt.R;
import com.codepath.photohunt.adapters.PhotoAdapter;
import com.codepath.photohunt.fragments.SearchPreferencesFragment;
import com.codepath.photohunt.helpers.EndlessScrollListener;
import com.codepath.photohunt.models.Photo;
import com.codepath.photohunt.models.imageType;
import com.codepath.photohunt.models.size;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements SearchPreferencesFragment.OnFragmentInteractionListener {
    private static String currentQuery = "";
    private static String searchColor;
    private static imageType searchImageType;
    private static size searchSize;
    private static String searchSite = "";
    private Toolbar toolbar;
    private StaggeredGridView gdResultsStaggered;
    private SearchPreferencesFragment searchFrag;
    private PhotoAdapter aPhotos;
    private ArrayList<Photo> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // find views in the application
        setupViews(savedInstanceState);
        // Set a ToolBar to replace the ActionBar.
        setSupportActionBar(toolbar);

        setListeners();
        aPhotos = new PhotoAdapter(this, photos);
        gdResultsStaggered.setAdapter(aPhotos);

    }

    private void setListeners() {
        gdResultsStaggered.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if(totalItemsCount <= 56)
                    fetchResults(currentQuery, totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        gdResultsStaggered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageViewActivity.class);

                Photo photo = photos.get(position);
                i.putExtra("url", photo.getImageUrl());
                startActivity(i);
            }
        });
    }

    private void setupViews(Bundle savedInstanceState) {
        // get GridView
        gdResultsStaggered = (StaggeredGridView) findViewById(R.id.gvResultsStaggered);
        // get toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (savedInstanceState == null) {
            searchFrag = SearchPreferencesFragment.newInstance();
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();

        searchFrag.show(fm, "fragment_edit_name");
    }

    private void hideEditDialog() {
        searchFrag.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem settingsItem = menu.findItem(R.id.action_search_settings);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showEditDialog();
                return true;
            }
        });
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;

                // clear the list
                photos.clear();
                aPhotos.notifyDataSetChanged();
                // Fetch the data remotely
                fetchResults(query, 0);

                // Reset SearchView
                searchView.setIconified(false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        final MenuItem shareItem = menu.findItem(R.id.action_search_share);
        shareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        return true;
    }

    private void fetchResults(String query, final int startIndex) {
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        if(startIndex >= 0)
            query += "&start=" + startIndex;
        if(null != searchColor && !searchColor.isEmpty())
            query += "&imgcolor=" + searchColor.toLowerCase();
        if(null != searchImageType)
            query += "&imgtype=" + searchImageType.toString().toLowerCase();
        if(null != searchSize)
            query += "&imgsz=" + searchSize.toString().toLowerCase();
        if(!searchSite.isEmpty())
            query += "&as_sitesearch=" + searchSite;

        //URI to get the JSON stream data array of countries
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + query;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON;
                try {
                    photosJSON = response.getJSONObject("responseData").getJSONArray("results");
                    Log.i("DEBUG Json array length:", Integer.toString(photosJSON.length()));
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        Photo photo = new Photo();

                        photo.setImageUrl(photoJSON.getString("url"));

                        photos.add(photo);
                    }
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

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(int sizeIndex, String color, int typeIndex, String website) {
        searchSize = (sizeIndex > -1) ? size.values()[sizeIndex]: null;
        searchColor = color;
        searchImageType = (typeIndex > -1) ? imageType.values()[typeIndex] : null;
        searchSite = website;

        // assume this is new search, so clear and run query again
        if(!currentQuery.isEmpty()) {
            photos.clear();
            fetchResults(currentQuery, 0);
        }
        hideEditDialog();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}

