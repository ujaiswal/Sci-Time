package com.akaashiitkgp.sci_time.view.viewgroup;

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

import com.akaashiitkgp.sci_time.R;

public class MenuFragment extends Fragment implements OnClickListener {
	
	public interface MenuFragmentListener {
		public void changeFragment();
	}
	
	private int selectedMenuItem = R.id.tab_timeline;
	private MenuFragmentListener listener;
	
	RelativeLayout timeline;
	RelativeLayout discovery_tree;
	RelativeLayout about;
	
	TextView text_timeline;
	TextView text_discovery_tree;
	TextView text_about;
	
	View fragmentView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.menu_list, container, false);
		
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

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
			selectedMenuItem = R.id.tab_timeline;
			break;
		case R.id.tab_discovery_tree:
			fragmentView.findViewById(selectedMenuItem).setBackgroundColor(res.getColor(R.color.Transparent));
			discovery_tree.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			selectedMenuItem = R.id.tab_discovery_tree;
			break;
		case R.id.tab_about:
			fragmentView.findViewById(selectedMenuItem).setBackgroundColor(res.getColor(R.color.Transparent));
			about.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			selectedMenuItem = R.id.tab_about;
			break;
		default:
			break;
		}
	}
	
}
