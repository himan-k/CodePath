package com.codepath.apps.tweeter.models;

import com.activeandroid.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Himanshu on 2/21/2015.
 */

public class Tweet extends Model {

    static private SimpleDateFormat twitterDateFormatter = null;
    private String body;
    private long tweetId;
    private Date createdAt;
    private User user;
    private Tweet retweetedStatus;

    public static Tweet fromJson(JSONObject object) {
        Tweet tweet = new Tweet();

        // create the SimpleDateFormat only once, on demand
        if (twitterDateFormatter == null) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            twitterDateFormatter = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            twitterDateFormatter.setLenient(true);
        }

        try {

            tweet.body = object.getString("text");
            tweet.tweetId = object.getLong("id");
            tweet.createdAt = twitterDateFormatter.parse(object.getString("created_at"));
            tweet.user = User.fromJson(object.getJSONObject("user"));

            JSONObject retweetedStatus = object.optJSONObject("retweeted_status");
            if (retweetedStatus != null)
                tweet.retweetedStatus = Tweet.fromJson(retweetedStatus);

        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject tweetJson;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return tweetId;
    }

    public User getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Tweet getRetweetedStatus() {
        return retweetedStatus;
    }
}