/*******************************************************************************
    Copyright 2013 Utkarsh Jaiswal

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 *******************************************************************************/
package com.aakashiitkgp.sci_time.view.viewgroup;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aakashiitkgp.sci_time.R;

public class MenuFragment extends Fragment implements OnClickListener {
	// Communicate with the main activity.
	public interface MenuFragmentListener {	
		public void changeFragment(int selectedFragment);
	}
	/**
	 * The current selected menu item.
	 */
	private int selectedMenuItem = R.id.tab_timeline;
	/**
	 * The fragment listener.
	 */
	private MenuFragmentListener listener;
	/**
	 * Item layouts.
	 */
	RelativeLayout timeline;
	RelativeLayout discovery_tree;
	RelativeLayout about;
	/**
	 * Item names.
	 */
	TextView text_timeline;
	TextView text_discovery_tree;
	TextView text_about;
	/**
	 * The fragment view.
	 */
	View fragmentView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Handle orientation changes.
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.menu_list, container, false);
		// Set up everything.
		timeline = (RelativeLayout) fragmentView.findViewById(R.id.tab_timeline);
		timeline.setOnClickListener(this);
		
		discovery_tree = (RelativeLayout) fragmentView.findViewById(R.id.tab_discovery_tree);
		discovery_tree.setOnClickListener(this);
		
		about = (RelativeLayout) fragmentView.findViewById(R.id.tab_about);
		about.setOnClickListener(this);
		
		Typeface app_font = Typeface.createFromAsset(getResources().getAssets(), "fonts/neuropol_x.ttf");
		
		text_timeline = (TextView) fragmentView.findViewById(R.id.text_timeline);
		text_timeline.setTypeface(app_font);
		
		text_discovery_tree = (TextView) fragmentView.findViewById(R.id.text_discovery_tree);
		text_discovery_tree.setTypeface(app_font);
		
		text_about = (TextView) fragmentView.findViewById(R.id.text_about);
		text_about.setTypeface(app_font);
		
		fragmentView.findViewById(selectedMenuItem).setBackgroundColor(getResources().getColor(R.color.DarkerBlue));
		
		return fragmentView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Initialize listener.
		if (activity instanceof MenuFragmentListener) {
			 listener = (MenuFragmentListener) activity;
			 }
		 else {
			 throw new ClassCastException(activity.toString() + " must implemenet MenuFragmentListener");
			 }
	}

	// Click actions for the menu list items.
	@Override
	public void onClick(View v) {
		
		Resources res = getResources();
		int viewId = v.getId();
		
		if(viewId == selectedMenuItem) {
			return;
		}
		
		switch (viewId) {
		case R.id.tab_timeline:
			fragmentView.findViewById(selectedMenuItem).setBackgroundColor(res.getColor(R.color.Transparent));
			timeline.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			selectedMenuItem = viewId;
			listener.changeFragment(selectedMenuItem);
			break;
		case R.id.tab_discovery_tree:
			fragmentView.findViewById(selectedMenuItem).setBackgroundColor(res.getColor(R.color.Transparent));
			discovery_tree.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			selectedMenuItem = viewId;
			listener.changeFragment(selectedMenuItem);
			break;
		case R.id.tab_about:
			fragmentView.findViewById(selectedMenuItem).setBackgroundColor(res.getColor(R.color.Transparent));
			about.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			selectedMenuItem = viewId;
			listener.changeFragment(selectedMenuItem);
			break;
		default:
			break;
		}
	}
	
}
