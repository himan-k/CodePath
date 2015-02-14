package com.codepath.photohunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl())
                .into(ivPhoto);


        return convertView;
    }
}
