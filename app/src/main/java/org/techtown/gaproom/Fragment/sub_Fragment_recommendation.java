package org.techtown.gaproom.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.gaproom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class sub_Fragment_recommendation extends Fragment {


    public sub_Fragment_recommendation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_recommendation, container, false);
    }

}
