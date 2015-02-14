package com.codepath.photohunt;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Himanshu on 2/14/2015.
 */
public class PhotoAdapter extends ArrayAdapter<Photo>{
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param objects The objects to represent in the ListView.
     */
    public PhotoAdapter(Context context, List<Photo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
