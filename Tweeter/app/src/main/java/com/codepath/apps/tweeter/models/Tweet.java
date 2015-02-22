package com.codepath.apps.tweeter.models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Himanshu on 2/21/2015.
 */
@Table(name = "tweets")
public class Tweet extends Model {

    static private SimpleDateFormat twitterDateFormatter = null;

    // Define table fields
    @Column(name = "body")
    private String body;

    @Column(name = "tweetId")
    private long tweetId;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "user")
    private User user;

    @Column(name = "retweetedStatus")
    private Tweet retweetedStatus;

    public Tweet() {
        super();
    }

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
        // save to database
        tweet.user.save();
        tweet.save();

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        //insert into database
        ActiveAndroid.beginTransaction();
        try {

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
                tweet.user.save();
                tweet.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return tweets;
    }

    public static List<Tweet> getLatestCount(String orderBy, int count) {
        return new Select()
                .from(Tweet.class)
                        //.where("Category = ?", category.getId())
                .orderBy(orderBy)
                .limit(count)
                .execute();
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