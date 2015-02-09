package com.codepath.fotogram;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by Himanshu on 2/7/2015.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhoto photo = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivProfilePhoto = (ImageView) convertView.findViewById(R.id.ivProfilePhoto);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        // set the caption
        tvCaption.setText(photo.getUsername() + " " + photo.getCaption(), TextView.BufferType.SPANNABLE);
        Spannable s = (Spannable)tvCaption.getText();
        int start = photo.getUsername().length();
        int end = start + photo.getCaption().length();
        s.setSpan(new ForegroundColorSpan(0xff406e95), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // set user name
        tvUsername.setText(photo.getUsername());

        // set timestamp
        String[] strSplit = ((String)DateUtils.getRelativeTimeSpanString(photo.getTimeStamp() * 1000,
                System.currentTimeMillis(),
                DateUtils.FORMAT_ABBREV_ALL))
                .split("\\s+");
        tvTimeStamp.setText(strSplit[0] + (strSplit[1]).charAt(0));

        // set Likes
        tvLikes.setText(photo.getLikesCount() + " likes");

        // set comments
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        tvComments.setText(Html.fromHtml(photo.getComments()), TextView.BufferType.SPANNABLE);
        s = (Spannable)tvComments.getText();
        //int start = photo.getUsername().length();
        //int end = start + photo.getCaption().length();
        //s.setSpan(new ForegroundColorSpan(0xff406e95), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // set images
        ivProfilePhoto.setImageResource(0);
        //Picasso.with(getContext()).load(photo.getProfilePicture()).into(ivProfilePhoto);
        Picasso.with(getContext()).load(photo.getProfilePicture()).
                transform(new RoundedTransformation(10, 20)).into(ivProfilePhoto);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl())
                .into(ivPhoto);
        return convertView;
    }
}
