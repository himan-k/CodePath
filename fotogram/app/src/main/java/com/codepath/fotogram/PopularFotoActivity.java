package com.codepath.fotogram;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularFotoActivity extends Activity {
    public final static String CLIENT_ID = "e8b4af12908d47979dc1dd2f90346ad4";
    public static String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    public static String commentsUrl = "https://api.instagram.com/v1/media//comments?client_id=" + CLIENT_ID;
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);

        //Set Custom font title
        int titleId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        } else {
            titleId = R.id.action_bar_title;
        }
        // Get access to our TextView
        TextView txt = (TextView) findViewById(titleId);
        txt.setTextSize(getResources().getDimension(R.dimen.titlebar_title_size));
        txt.setTypeface(null, Typeface.BOLD);
        // Create the TypeFace from the TTF asset
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AlexBrush-Regular.ttf");

        // Assign the typeface to the view
        txt.setTypeface(font);
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotoAdapter(this, photos);

        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos(false);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos(true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvPhotos.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchPopularPhotos(false);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    public void fetchPopularPhotos(final boolean addFront) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    Log.i("DEBUG Json array length:", Integer.toString(photosJSON.length()));
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                            photo.setTimeStamp(photoJSON.getJSONObject("caption").getLong("created_time"));
                        }
                        photo.setImageUrl(photoJSON.getJSONObject("images").
                                getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").
                                getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setProfilePicture(photoJSON.getJSONObject("user").getString("profile_picture"));

                        photo.setIdMedia(photoJSON.getString("id"));
                        if (addFront)
                            photos.add(0, photo);
                        else
                            photos.add(photo);
                        swipeContainer.setRefreshing(false);
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

   /* public String fetchComments(InstagramPhoto photo) {

        AsyncHttpClient client = new AsyncHttpClient();
        String finalUrl = new StringBuilder(commentsUrl).insert(34, photo.getIdMedia()).toString();
        final String[] Comments = {null};
        client.get(finalUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray commentsJSON = null;
                try {
                    commentsJSON = response.getJSONArray("data");
                    Log.i("DEBUG Json array length:", Integer.toString(commentsJSON.length()));

                    for (int i = 0; i < commentsJSON.length(); i++) {
                        JSONObject commentJSON = commentsJSON.getJSONObject(i);
                        if(!commentJSON.isNull("from")){
                            Comments[0] += "<b>" + commentJSON.getJSONObject("from").getString("username") + "</b>";
                            Comments[0] += " " + commentJSON.getJSONObject("text").toString();
                        }
                        Comments[0] += "\n";

                        if(commentsJSON.length() > 4 && i == 2) {
                            Comments[0] += "view all " + commentsJSON.length() + " comments\n";
                            i = commentsJSON.length() - 3;
                        }
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
        return Comments[0];
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular, menu);
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
}
