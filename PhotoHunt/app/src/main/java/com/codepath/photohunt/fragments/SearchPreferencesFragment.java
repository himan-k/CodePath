package com.codepath.photohunt.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.codepath.photohunt.R;
import com.codepath.photohunt.helpers.ToggleButtonGroupTableLayout;

import at.markushi.ui.CircleButton;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchPreferencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchPreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPreferencesFragment extends DialogFragment {
    private static ViewHolder viewHolder = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchPreferencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchPreferencesFragment newInstance() {
        SearchPreferencesFragment fragment = new SearchPreferencesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchPreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments here if you need to
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Search Preferences");
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_preferences, container, false);
        if(null == ((ViewHolder) view.getTag())){
            viewHolder = new ViewHolder();
            viewHolder.sgSize = (SegmentedGroup) view.findViewById(R.id.segmentedSize);
            viewHolder.rgColor = (ToggleButtonGroupTableLayout) view.findViewById(R.id.rgColor);
            viewHolder.sgType = (SegmentedGroup) view.findViewById(R.id.segmentedType);
            viewHolder.website = (EditText) view.findViewById(R.id.etWebsite);
            viewHolder.cbSave = (CircleButton) view.findViewById(R.id.cbSave);
            viewHolder.cbClear = (CircleButton) view.findViewById(R.id.cbClear);

            viewHolder.cbSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int size = viewHolder.sgSize.indexOfChild(
                                viewHolder.sgSize.findViewById(
                                        viewHolder.sgSize.getCheckedRadioButtonId()
                                )
                        );
                        int color = viewHolder.rgColor.indexOfChild(
                                (RadioButton)viewHolder.rgColor.findViewById(
                                viewHolder.rgColor.getCheckedRadioButtonId()
                                )
                        );
                        int type = viewHolder.sgType.indexOfChild(
                                viewHolder.sgType.findViewById(
                                        viewHolder.sgType.getCheckedRadioButtonId()
                                )
                        );
                        String website = viewHolder.website.getText().toString();
                        mListener.onFragmentInteraction(size, color, type, website);
                    }
                }
            });

            viewHolder.cbClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.sgSize.clearCheck();
                    viewHolder.sgType.clearCheck();
                    RadioButton btn = (RadioButton)viewHolder.rgColor.findViewById(
                            viewHolder.rgColor.getCheckedRadioButtonId()
                        );
                    if(null != btn)
                        btn.setChecked(false);
                    viewHolder.website.setText("");
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int sizeIndex, int colorIndex, int typeIndex, String website);
    }

    static class ViewHolder {
        SegmentedGroup sgSize;
        ToggleButtonGroupTableLayout rgColor;
        SegmentedGroup sgType;
        EditText website;
        CircleButton cbSave;
        CircleButton cbClear;
    }

}
