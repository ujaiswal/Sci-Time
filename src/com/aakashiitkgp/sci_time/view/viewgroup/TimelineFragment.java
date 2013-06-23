package com.aakashiitkgp.sci_time.view.viewgroup;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aakashiitkgp.sci_time.R;
import com.aakashiitkgp.sci_time.controller.Sci_Time;

public class TimelineFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	/**
	 * The list view of year ranges;
	 */
	PopupWindow pw;
	ListView yearList;
	SimpleCursorAdapter yearListAdapter;
	Cursor yearListCursor;
	TextView yearListDropdown;
	/**
	 * Set up list view for discoveries
	 */
	ListView discoveryList;
	SimpleCursorAdapter discoveryListAdapter;
	Cursor discoveryListCursor;
	
	TextView fragmentHeading;
	
	private TimelineFragmentListener listener;
	
	public Handler handler;
	private String selectedYearRange = "--Select--";
	
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
		
		Typeface appFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/neuropol_x.ttf");
		
		// Set up list view of year ranges.
		yearListDropdown = (TextView) fragmentView.findViewById(R.id.year_list_dropdown);
		yearListDropdown.setTypeface(appFont);
		yearListDropdown.setText(selectedYearRange);
		yearListDropdown.setOnClickListener(this);
		
		// Set up list view of discoveries.
		discoveryList = (ListView) fragmentView.findViewById(R.id.listDiscovery);
		
		fragmentHeading = (TextView) fragmentView.findViewById(R.id.text_fragment_timeline);
		fragmentHeading.setTypeface(appFont);
		
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
		
		if(yearListCursor == null) {
			listener.getYearRanges(handler);
		}
		yearListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.year_list, yearListCursor, new String [] {"Name"}, new int [] {R.id.TableListItem}, 0);

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
					fragment.yearListCursor = (Cursor) msg.obj;
					fragment.yearListAdapter.changeCursor(fragment.yearListCursor);
					break;
				case Sci_Time.TRANSMIT_DISCOVERIES:
					fragment.discoveryListCursor = (Cursor) msg.obj;
					fragment.discoveryListAdapter.changeCursor(fragment.discoveryListCursor);
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
				yearListDropdown.setText(selectedYearRange);
				pw.dismiss();
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
		
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.year_list_dropdown:
				initPopUpWindow();
				break;
			default:
				break;
			}
		}
		
		private void initPopUpWindow () {
			
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			yearList = (ListView) inflater.inflate(R.layout.year_listview, null);
			
			pw = new PopupWindow(yearList, getResources().getDimensionPixelSize(R.dimen.year_list_width), LayoutParams.WRAP_CONTENT, true);
	    	
			//Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
	    	pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.content_background));
	    	pw.setTouchable(true);
	    	
	    	//let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
	    	pw.setOutsideTouchable(true);
	    	
	    	//dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
	    	pw.setTouchInterceptor(new OnTouchListener() {
	    		
	    		public boolean onTouch(View v, MotionEvent event) {
	    			// TODO Auto-generated method stub
	    			if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
	    				pw.dismiss();
	        			return true;    				
	    			}
	    			return false;
	    		}
	    	});
	    	
	    	//populate the drop-down list
	    	yearList.setAdapter(yearListAdapter);
			yearList.setOnItemClickListener(this);
			
			//anchor the drop-down to bottom-left
	    	pw.showAsDropDown(yearListDropdown);
		}
		
   // Disposing of objects.
		@Override
		public void onDestroy() {
			super.onDestroy();
			yearListCursor.close();
			if(discoveryListCursor != null) {
				discoveryListCursor.close();
			}
			handler = null;
			selectedYearRange = null;
		}

}
