package com.codepath.apps.tweeter.models;

/**
 * Created by Himanshu on 2/21/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "users")
public class User extends Model implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
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

    protected User(Parcel in) {
        name = in.readString();
        userId = in.readLong();
        screenName = in.readString();
        profileImageUrl = in.readString();
        tagLine = in.readString();
        numFollowers = in.readInt();
        numFollowing = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(userId);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
        dest.writeString(tagLine);
        dest.writeInt(numFollowers);
        dest.writeInt(numFollowing);
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