package com.akaashiitkgp.sci_time.view.viewgroup;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.akaashiitkgp.sci_time.R;

public class TimelineFragment extends Fragment implements OnItemClickListener {
	
	/**
	 * The list view of year ranges;
	 */
	ListView yearList;
	SimpleCursorAdapter yearListAdapter;
	/**
	 * Set up list view for discoveries
	 */
	ListView discoveryList;
	SimpleCursorAdapter discoveryListAdapter;
	
	TextView fragmentHeading;
	
	private ContentFragmentListener listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View fragmentView = inflater.inflate(R.layout.timeline, container, false);
		
		// Set up list view of year ranges.
		yearList = (ListView) fragmentView.findViewById(R.id.listYear);
		
		// Set up list view of discoveries.
		discoveryList = (ListView) fragmentView.findViewById(R.id.listDiscovery);
		
		fragmentHeading = (TextView) fragmentView.findViewById(R.id.text_fragment_timeline);
		fragmentHeading.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/neuropol_x.ttf"));
		
		return fragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		 if (activity instanceof ContentFragmentListener) {
			 listener = (ContentFragmentListener) activity;
			 }
		 else {
			 throw new ClassCastException(activity.toString() + " must implemenet MyListFragment.OnItemSelectedListener");
			 }
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (yearListAdapter ==  null) {
			yearListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.table_list, listener.getYearRanges(), new String [] {"Name"}, new int [] {R.id.TableListItem}, 0);
		}
		yearList.setAdapter(yearListAdapter);
		yearList.setOnItemClickListener(this);
		
		if (discoveryListAdapter == null) {
			discoveryListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.discovery_list, null, new String [] {"Year", "Discovery", "Discoverer"}, new int [] {R.id.text_year, R.id.text_discovery, R.id.text_discoverer}, 0);
		}
		discoveryList.setAdapter(discoveryListAdapter);
	}
	
	public interface ContentFragmentListener {
		public Cursor getYearRanges();
		public Cursor getDiscoveries(String yearRange);
	}

	// Setting click action for lists.
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Cursor c = (Cursor) parent.getItemAtPosition(position);
			discoveryListAdapter.changeCursor(listener.getDiscoveries(c.getString(1)));		
		}

}
