package com.codepath.apps.tweeter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;

import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.codepath.apps.tweeter.fragments.ComposeFragment.OnComposedListener} interface
 * to handle interaction events.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment {
    private static ViewHolder viewHolder = null;
    private OnComposedListener mListener;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance() {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Compose Tweet");
        int textViewId = getDialog().getContext().getResources().getIdentifier("android:id/title", null, null);
        TextView tv = (TextView) getDialog().findViewById(textViewId);
        tv.setBackgroundColor(getResources().getColor(R.color.CornflowerBlue));
        tv.setTextColor(getResources().getColor(R.color.White));
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_compose, container, false);
        if (null == (view.getTag())) {
            viewHolder = new ViewHolder();
            viewHolder.tweetBtn = (FButton) view.findViewById(R.id.f_twitter_button);
            viewHolder.etBody = (EditText) view.findViewById(R.id.etBody);
            viewHolder.tweetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText tweet = (EditText) getDialog().findViewById(R.id.etBody);
                    if (mListener != null && tweet.getText() != null) {
                        mListener.onComposed(tweet.getText().toString());
                    }
                    tweet.setText("");
                }
            });
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnComposedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnComposedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnComposedListener {
        public void onComposed(String tweet);
    }

    static class ViewHolder {
        FButton tweetBtn;
        EditText etBody;
    }

}
