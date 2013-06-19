package com.aakashiitkgp.sci_time.view.viewgroup;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aakashiitkgp.sci_time.R;

public class AboutFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View fragmentView = inflater.inflate(R.layout.about, container, false);
		
		TextView heading = (TextView) fragmentView.findViewById(R.id.aakash_heading);
		Typeface app_font = Typeface.createFromAsset(getResources().getAssets(), "fonts/neuropol_x.ttf");
		heading.setTypeface(app_font);
		
		return fragmentView;
	}

}
