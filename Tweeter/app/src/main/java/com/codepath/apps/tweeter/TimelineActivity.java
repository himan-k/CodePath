package com.codepath.apps.tweeter;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.tweeter.models.HomeTimelineFragment;
import com.codepath.apps.tweeter.models.MentionsFragment;

public class TimelineActivity extends ActionBarActivity {
    private FragmentTabHost tabHost;
    private HomeTimelineFragment timelineFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // get the tab host
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // add the two tabs we want, referencing the views that include
        // the appropriate fragments
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Home"), HomeTimelineFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Mentions"), MentionsFragment.class, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
