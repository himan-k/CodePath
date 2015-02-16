package com.codepath.photohunt;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by Himanshu on 2/14/2015.
 */
public class PhotoAdapter extends ArrayAdapter<Photo>{
    private static final String TAG = "SampleAdapter";
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param objects The objects to represent in the ListView.
     */
    public PhotoAdapter(Context context, List<Photo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRandom = new Random();
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
        ViewHolder viewHolder;
        Photo photo = getItem(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.staggered_grid_custom_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgView = (DynamicHeightImageView) convertView
                    .findViewById(R.id.imgView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        viewHolder.imgView.setHeightRatio(positionHeight);

        viewHolder.imgView.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl())
                .placeholder(getContext().getResources()
                        .getDrawable(R.drawable.user_placeholder))
                .error(getContext().getResources()
                        .getDrawable(R.drawable.user_placeholder))
                .into(viewHolder.imgView);

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done, generate and stash the columns height
        // In our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }
    static class ViewHolder {
        DynamicHeightImageView imgView;
    }
}
