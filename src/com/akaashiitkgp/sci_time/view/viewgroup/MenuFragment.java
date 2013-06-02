package com.akaashiitkgp.sci_time.view.viewgroup;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.akaashiitkgp.sci_time.R;

public class MenuFragment extends Fragment implements OnClickListener {
	
	private enum MenuItem {
		TIMELINE, DISCOVERY_TREE, ABOUT
	}
	
	public interface MenuFragmentListener {
		
	}
	
	private MenuItem selectedMenuItem = MenuItem.TIMELINE;
	private MenuFragmentListener listener;
	
	RelativeLayout timeline;
	RelativeLayout discovery_tree;
	RelativeLayout about;
	
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
		
		onItemSelect();
		
		return fragmentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_timeline:
			selectedMenuItem = MenuItem.TIMELINE;
			onItemSelect();
			break;
		case R.id.tab_discovery_tree:
			selectedMenuItem = MenuItem.DISCOVERY_TREE;
			onItemSelect();
			break;
		case R.id.tab_about:
			selectedMenuItem = MenuItem.ABOUT;
			onItemSelect();
			break;
		default:
			break;
		}
	}
	
	
	private void onItemSelect() {
		Resources res = getResources();
		
		switch (selectedMenuItem) {
		case TIMELINE:
			timeline.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			discovery_tree.setBackgroundColor(res.getColor(R.color.Transparent));
			about.setBackgroundColor(res.getColor(R.color.Transparent));
			break;
		case DISCOVERY_TREE:
			timeline.setBackgroundColor(res.getColor(R.color.Transparent));
			discovery_tree.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			about.setBackgroundColor(res.getColor(R.color.Transparent));
			break;
		case ABOUT:
			timeline.setBackgroundColor(res.getColor(R.color.Transparent));
			discovery_tree.setBackgroundColor(res.getColor(R.color.Transparent));
			about.setBackgroundColor(res.getColor(R.color.DarkerBlue));
			break;
		default:
			break;
		}
		
		fragmentView.invalidate();
	}
}
