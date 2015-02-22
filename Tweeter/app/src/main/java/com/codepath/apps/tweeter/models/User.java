package com.codepath.apps.tweeter.models;

/**
 * Created by Himanshu on 2/21/2015.
 */

import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User extends Model implements Serializable {

    private String name;
    private long userId;
    private String screenName;
    private String profileImageUrl;
    private String tagLine;
    private int numFollowers;
    private int numFollowing;

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();

        try {

            user.name = jsonObject.getString("name");
            user.userId = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagLine = jsonObject.getString("description");
            user.numFollowers = jsonObject.getInt("followers_count");
            user.numFollowing = jsonObject.getInt("friends_count");

        } catch (JSONException e) {
            return null;
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }
}