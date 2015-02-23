package com.codepath.apps.tweeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.fragments.ComposeFragment;
import com.codepath.apps.tweeter.listeners.TweetComposedListener;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailsActivity extends ActionBarActivity implements TweetComposedListener {
    ImageView ivStatus, ivProfileImage;
    TextView tvStatus, tvUsername, tvScreenName, tvBody, tvPostTime;
    private ComposeFragment replyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();

        Tweet curr_tweet = getIntent().getParcelableExtra("tweet");
        User curr_tweetUser = curr_tweet.getUser();

        Date createdAtDate = curr_tweet.getCreatedAt();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        populateControls(curr_tweet, curr_tweetUser, createdAtDate, imageLoader);

        if (savedInstanceState == null) {
            replyFragment = ComposeFragment.newInstance(getReplyToUsers());
        }
    }

    private String getReplyToUsers() {
        String Users = tvScreenName.getText().toString();
        Matcher matcher = Pattern.compile("@\\p{L}+").matcher(tvBody.getText().toString());
        while (matcher.find()) {
            Users += " " + matcher.group();
        }
        return Users;
    }

    private void populateControls(Tweet curr_tweet, User curr_tweetUser, Date createdAtDate, ImageLoader imageLoader) {
        // decide whether this is a retweet
        Tweet retweetedStatus = curr_tweet.getRetweetedStatus();
        if (retweetedStatus != null) {
            ivStatus.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(curr_tweetUser.getName() + " retweeted");

            curr_tweet = retweetedStatus;
            curr_tweetUser = curr_tweet.getUser();

        } else {
            ivStatus.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
        }

        // push fields appropriately
        tvBody.setText(Html.fromHtml(curr_tweet.getBody()));
        tvUsername.setText(curr_tweetUser.getName());
        tvScreenName.setText("@" + curr_tweetUser.getScreenName());
        imageLoader.displayImage(curr_tweetUser.getProfileImageUrl(), ivProfileImage);

        // figure out the relative time; default to a hard-coded 'now' if the tweet
        // appears to be in the future — that would just imply that the local clock
        // slightly disagrees with the Twitter clock
        long createdAtTime = createdAtDate.getTime();
        long timeNow = System.currentTimeMillis();
        if (createdAtTime < timeNow) {
            // set timestamp
            tvPostTime.setText(curr_tweet.getCreatedAt().toString());
        }
    }

    private void setupViews() {
        setContentView(R.layout.activity_details);
        ivStatus = (ImageView) findViewById(R.id.ivStatus);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvStatus = (TextView) findViewById(R.id.tvStatusMessage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvPostTime = (TextView) findViewById(R.id.tvPostTime);
    }

    public void onReply(View view) {
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();

        replyFragment.show(fm, "fragment_reply");
    }

    private void hideEditDialog() {
        replyFragment.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

    @Override
    public void onComposed(String reply) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("reply", reply);
        setResult(RESULT_OK, returnIntent);
        hideEditDialog();
        finish();
    }
}
