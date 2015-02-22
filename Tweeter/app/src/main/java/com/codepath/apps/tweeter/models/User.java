package com.codepath.apps.tweeter.models;

/**
 * Created by Himanshu on 2/21/2015.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(name = "users")
public class User extends Model implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "userId", unique = true)
    private long userId;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    @Column(name = "tagLine")
    private String tagLine;
    @Column(name = "numFollowers")
    private int numFollowers;
    @Column(name = "numFollowing")
    private int numFollowing;

    public User() {
        super();
    }

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
        user.save();
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