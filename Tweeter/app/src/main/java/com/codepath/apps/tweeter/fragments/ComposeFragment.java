package com.codepath.apps.tweeter.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.listeners.TweetComposedListener;

import info.hoang8f.widget.FButton;

public class ComposeFragment extends DialogFragment {
    private static final int TWEET_MAX_LENGTH = 140;
    private static ViewHolder viewHolder = null;
    private static String replyUsers = null;
    private TweetComposedListener mListener;

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance(String replyUsers) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("replyUsers", replyUsers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        replyUsers = getArguments().getString("replyUsers", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SetupComposeFragHeader();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_compose, container, false);

        if (null == (view.getTag())) {
            SetupViews(view);
            SetupListeners();
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private void SetupViews(View view) {
        viewHolder = new ViewHolder();
        viewHolder.tweetBtn = (FButton) view.findViewById(R.id.f_twitter_button);
        viewHolder.etBody = (EditText) view.findViewById(R.id.etBody);
        viewHolder.tvCharRemaining = (TextView) view.findViewById(R.id.tvCharRemaining);

        if (null != replyUsers) {
            viewHolder.etBody.setText(replyUsers);
            viewHolder.etBody.setSelection(replyUsers.length());
            viewHolder.tvCharRemaining.setText(replyUsers.length() + "");
        }
    }

    private void SetupListeners() {
        SetRemainingCharsListener();
        SetPostTweetListener();
    }

    private void SetPostTweetListener() {
        viewHolder.tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && viewHolder.etBody.getText() != null) {
                    mListener.onComposed(viewHolder.etBody.getText().toString());
                }
                viewHolder.etBody.setText("");
                viewHolder.tvCharRemaining.setText(String.valueOf(TWEET_MAX_LENGTH));
            }
        });
    }

    private void SetRemainingCharsListener() {
        viewHolder.etBody.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = TWEET_MAX_LENGTH - s.length();
                viewHolder.tvCharRemaining.setText(String.valueOf(count));
                if (count > 10) {
                    viewHolder.tvCharRemaining.setTextColor(Color.GRAY);
                } else if (count > 5) {
                    viewHolder.tvCharRemaining.setTextColor(Color.rgb(240, 190, 40)); // orange
                } else {
                    viewHolder.tvCharRemaining.setTextColor(Color.RED);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TweetComposedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void SetupComposeFragHeader() {
        getDialog().setTitle("Compose Tweet");
        int textViewId = getDialog().getContext().getResources().getIdentifier(
                "android:id/title",
                null,
                null);
        TextView tv = (TextView) getDialog().findViewById(textViewId);
        tv.setBackgroundColor(getResources().getColor(R.color.CornflowerBlue));
        tv.setTextColor(getResources().getColor(R.color.White));
    }

    static class ViewHolder {
        FButton tweetBtn;
        EditText etBody;
        TextView tvCharRemaining;
    }

}
