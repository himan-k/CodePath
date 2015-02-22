package com.codepath.apps.tweeter.adapters;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Date;
import java.util.List;

/**
 * Created by Himanshu on 2/21/2015.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public TweetListAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // idiomatic viewholder stuff; grab the subviews and store them as
        // a tag, if we haven't already. Otherwise just grab the tag.
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.ivStatus = (ImageView) convertView.findViewById(R.id.ivStatus);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatusMessage);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvPostTime = (TextView) convertView.findViewById(R.id.tvPostTime);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // clear any persisting profile image from a previous view use
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);

        // get the tweet, the user and the image uploader
        Tweet tweet = getItem(position);
        User user = tweet.getUser();
        Date createdAtDate = tweet.getCreatedAt();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this.getContext()));

        // decide whether this is a retweet
        Tweet retweetedStatus = tweet.getRetweetedStatus();
        if (retweetedStatus != null) {
            viewHolder.ivStatus.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setText(user.getName() + " retweeted");

            tweet = retweetedStatus;
            user = tweet.getUser();

        } else {
            viewHolder.ivStatus.setVisibility(View.GONE);
            viewHolder.tvStatus.setVisibility(View.GONE);
        }

        // push fields appropriately
        viewHolder.tvBody.setText(Html.fromHtml(tweet.getBody()));
        viewHolder.tvUsername.setText(user.getName());
        viewHolder.tvScreenName.setText("@" + user.getScreenName());
        imageLoader.displayImage(user.getProfileImageUrl(), viewHolder.ivProfileImage);

        // figure out the relative time; default to a hard-coded 'now' if the tweet
        // appears to be in the future — that would just imply that the local clock
        // slightly disagrees with the Twitter clock
        long createdAtTime = createdAtDate.getTime();
        long timeNow = System.currentTimeMillis();
        if (createdAtTime < timeNow) {
            // set timestamp
            String[] strSplit = ((String) DateUtils.getRelativeTimeSpanString(tweet.getCreatedAt().getTime(),
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS)
            ).split("\\s+");
            viewHolder.tvPostTime.setText(strSplit[0] + (strSplit[1]).charAt(0));

        } else {
            viewHolder.tvPostTime.setText("Now");
        }

        // add an on-click listener potentially to launch the profile page
//        final User finalUser = user;
//        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Context context = getContext();
//                Intent intent = new Intent(context, ProfileActivity.class);
//                intent.putExtra(ProfileActivity.EXTRA_USER, finalUser);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

    private class ViewHolder {
        ImageView ivStatus, ivProfileImage;
        TextView tvStatus, tvUsername, tvScreenName, tvBody, tvPostTime;
    }
}
