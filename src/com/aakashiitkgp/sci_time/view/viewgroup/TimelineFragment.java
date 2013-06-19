package com.aakashiitkgp.sci_time.view.viewgroup;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aakashiitkgp.sci_time.R;
import com.aakashiitkgp.sci_time.controller.Sci_Time;

public class TimelineFragment extends Fragment implements OnItemClickListener {
	
	/**
	 * The list view of year ranges;
	 */
	ListView yearList;
	SimpleCursorAdapter yearListAdapter;
	private static Cursor yearListCursor;
	/**
	 * Set up list view for discoveries
	 */
	ListView discoveryList;
	SimpleCursorAdapter discoveryListAdapter;
	private static Cursor discoveryListCursor;
	
	TextView fragmentHeading;
	
	private TimelineFragmentListener listener;
	
	public Handler handler;
	private static String selectedYearRange;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		
		handler = new DatabaseHandler(this);
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
		
		 if (activity instanceof TimelineFragmentListener) {
			 listener = (TimelineFragmentListener) activity;
			 }
		 else {
			 throw new ClassCastException(activity.toString() + " must implemenet TimelineFragmentListener");
			 }
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		yearListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.table_list, yearListCursor, new String [] {"Name"}, new int [] {R.id.TableListItem}, 0);
		if(yearListCursor == null) {
			listener.getYearRanges(handler);
		}

		yearList.setAdapter(yearListAdapter);
		yearList.setOnItemClickListener(this);

		discoveryListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.discovery_list, discoveryListCursor, new String [] {"Year", "Discovery", "Discoverer"}, new int [] {R.id.text_year, R.id.text_discovery, R.id.text_discoverer}, 0);
		
		discoveryList.setAdapter(discoveryListAdapter);
		discoveryList.setOnItemClickListener(this);
	}
	
	private static class DatabaseHandler extends Handler {
		private final WeakReference<TimelineFragment> mFragment;
		
		public DatabaseHandler(TimelineFragment fragment) {
			super();
			mFragment = new WeakReference<TimelineFragment>(fragment);
		}
		
		@Override
		public void handleMessage(Message msg) {
			TimelineFragment fragment = mFragment.get();
			
			if(fragment != null) {
				switch (msg.what) {
				case Sci_Time.TRANSMIT_YEAR_RANGE:
					yearListCursor = (Cursor) msg.obj;
					fragment.yearListAdapter.changeCursor(yearListCursor);
					break;
				case Sci_Time.TRANSMIT_DISCOVERIES:
					if(discoveryListCursor != null) {
						discoveryListCursor.close();
					}
					discoveryListCursor = (Cursor) msg.obj;
					fragment.discoveryListAdapter.changeCursor(discoveryListCursor);
					break;
				case Sci_Time.NO_TRANSMISSION_WAIT:
					Toast wait = Toast.makeText(fragment.getActivity(), "Loading...\nPlease wait!", Toast.LENGTH_SHORT);
					wait.show();
					break;
				default:
					break;
				}
		}
		}
	}

	public interface TimelineFragmentListener {
		public void getYearRanges(Handler handler);
		public void getDiscoveries(Handler handler, String yearRange);
	}

	// Setting click action for lists.
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			switch (view.getId()) {
			case R.id.year_list:
				Cursor c = (Cursor) parent.getItemAtPosition(position);
				
				if(c.getString(1).equals(selectedYearRange)) {
					return;
				}
				
				selectedYearRange = c.getString(1);
				listener.getDiscoveries(handler, selectedYearRange);
				break;
			case R.id.discovery_list:
				RelativeLayout listItem = (RelativeLayout) view;
				TextView textItem;
				String text = new String();
				for (int i = 0 ; i < listItem.getChildCount() ; i++ ) {
					textItem = (TextView) listItem.getChildAt(i);
					text += textItem.getText();
					text += ". ";
				}
				Sci_Time.speak(text);
				break;
			default:
				break;
			}
		}
		
   // Disposing of objects.
		@Override
		public void onDestroy() {
			super.onDestroy();
			handler = null;
			yearListCursor.close();
			yearListCursor = null;
			if(discoveryListCursor != null) {
				discoveryListCursor.close();
				discoveryListCursor = null;
			}
			selectedYearRange = null;
		}

}
