package com.example.wmm;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class StatisticsFragment extends Fragment {

	public StatisticsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {

    	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    	
    	return rootView;
    }
	
}
