package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HIMANSHU on 1/20/2015.
 */
public class TaskAdapter extends ArrayAdapter<Task>{
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super (context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvDueDate);
        // Populate the data into the template view using the data object
        tvTitle.setText(task.getTitle());
        tvHome.setText(task.getDueDate());
        // Return the completed view to render on screen
        return convertView;
    }
}
