package com.codepath.photohunt.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.photohunt.R;
import com.codepath.photohunt.helpers.RectangularTransformation;
import com.codepath.photohunt.helpers.TouchImageView;
import com.squareup.picasso.Picasso;


public class ImageViewActivity extends SearchActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        String url = getIntent().getStringExtra("url");
        TouchImageView ivImage = (TouchImageView) findViewById(R.id.ivImage);
        Picasso.with(this).load(url)
                .transform(new RectangularTransformation(10, 20))
                .skipMemoryCache()
                .resize(1080,1920)
                .centerCrop()
                .into(ivImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
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
