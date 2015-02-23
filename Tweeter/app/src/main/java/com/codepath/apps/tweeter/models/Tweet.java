package com.codepath.apps.tweeter.models;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Tweet extends Model implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
    static private SimpleDateFormat twitterDateFormatter = null;
    // Define table fields
    @Column(name = "body")
    private String body;
    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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

    protected Tweet(Parcel in) {
        twitterDateFormatter = (SimpleDateFormat) in.readValue(SimpleDateFormat.class.getClassLoader());
        body = in.readString();
        tweetId = in.readLong();
        long tmpCreatedAt = in.readLong();
        createdAt = tmpCreatedAt != -1 ? new Date(tmpCreatedAt) : null;
        user = (User) in.readValue(User.class.getClassLoader());
        retweetedStatus = (Tweet) in.readValue(Tweet.class.getClassLoader());
    }

    public static Tweet findOrCreateFromJson(JSONObject object) {
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
            tweet.user = User.findOrCreateFromJson(object.getJSONObject("user"));

            JSONObject retweetedStatus = object.optJSONObject("retweeted_status");
            if (retweetedStatus != null)
                tweet.retweetedStatus = Tweet.findOrCreateFromJson(retweetedStatus);

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

                Tweet tweet = Tweet.findOrCreateFromJson(tweetJson);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(twitterDateFormatter);
        dest.writeString(body);
        dest.writeLong(tweetId);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1L);
        dest.writeValue(user);
        dest.writeValue(retweetedStatus);
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