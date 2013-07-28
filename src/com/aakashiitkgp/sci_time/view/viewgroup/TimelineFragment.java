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

import java.lang.ref.WeakReference;

import android.animation.ObjectAnimator;
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
	
	// Communicate with the main activity.
	public interface TimelineFragmentListener {
		public void getYearRanges(Handler handler);
		public void getDiscoveries(Handler handler, String yearRange);
		public void getArticle(Handler handler, String yearRange, String title);
		public void startArticleActivity(Bundle extras);
	}

	/**
	 * The list view of year ranges.
	 */
	PopupWindow pw;
	ListView yearList;
	SimpleCursorAdapter yearListAdapter;
	Cursor yearListCursor;
	TextView yearListDropdown;
	/**
	 * The list view of discoveries.
	 */
	ListView discoveryList;
	SimpleCursorAdapter discoveryListAdapter;
	Cursor discoveryListCursor;
	/**
	 * The fragment heading.
	 */
	TextView fragmentHeading;
	/**
	 * The fragment listener.
	 */
	private TimelineFragmentListener listener;
	/**
	 * The database handler.
	 */
	public Handler handler;
	/**
	 * Current selected year range.
	 */
	private String selectedYearRange = "--Select--";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Handle orientation changes.
		setRetainInstance(true);
		
		// Initialize handler.
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
		
		// Set up fragment heading.
		fragmentHeading = (TextView) fragmentView.findViewById(R.id.text_fragment_timeline);
		fragmentHeading.setTypeface(appFont);
		
		return fragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Initialize the listener.
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
		
		// Initialize the parts requiring activity context.
		if(yearListCursor == null) {
			listener.getYearRanges(handler);
		}
		yearListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.year_list, yearListCursor, new String [] {"Name"}, new int [] {R.id.TableListItem}, 0);

		discoveryListAdapter = new AnimatedSimpleCursorAdapter(getActivity(), R.layout.discovery_list, discoveryListCursor, new String [] {"Title"}, new int [] {R.id.discovery_title}, 0);
		discoveryList.setAdapter(discoveryListAdapter);
		discoveryList.setOnItemClickListener(this);
	}
	
	// Setting click action for lists.
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		switch (view.getId()) {
		case R.id.year_list:
			pw.dismiss();
			Cursor c = (Cursor) parent.getItemAtPosition(position);
			if(c.getString(1).equals(selectedYearRange)) {
				return;
			}
			selectedYearRange = c.getString(1);
			yearListDropdown.setText(selectedYearRange);
			listener.getDiscoveries(handler, selectedYearRange);
			break;
		case R.id.discovery_list:
			RelativeLayout listItem = (RelativeLayout) view;
			TextView textItem;
			textItem = (TextView) listItem.getChildAt(0);
			String text = (String) textItem.getText();
			listener.getArticle(handler, (String) yearListDropdown.getText(), text);
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
		
	// Dispose-off objects.
	@Override
	public void onDestroy() {
		super.onDestroy();
		yearListCursor.close();
		if(discoveryListCursor != null) {
			discoveryListCursor.close();
		}
		handler = null;
	}

	// Initializes PopUp window.
	private void initPopUpWindow () {
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		yearList = (ListView) inflater.inflate(R.layout.year_listview, null);
		
		pw = new PopupWindow(yearList, getResources().getDimensionPixelSize(R.dimen.year_list_width), LayoutParams.WRAP_CONTENT, true);
    	
		// Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
    	pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.content_background));
    	pw.setTouchable(true);
    	
    	// let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
    	pw.setOutsideTouchable(true);
    	
    	// dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
    	pw.setTouchInterceptor(new OnTouchListener() {
    		
    		public boolean onTouch(View v, MotionEvent event) {
    			if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
    				pw.dismiss();
        			return true;    				
    			}
    			return false;
    		}
    	});
    	
    	// populate the drop-down list
    	yearList.setAdapter(yearListAdapter);
		yearList.setOnItemClickListener(this);
		
		// anchor the drop-down to bottom-left
    	pw.showAsDropDown(yearListDropdown);
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
					fragment.discoveryList.setSelection(0);
					break;
				case Sci_Time.TRANSMIT_ARTICLE:
					Cursor c = (Cursor) msg.obj;
					Bundle extras = new Bundle();
					extras.putString("Title", c.getString(1));
					extras.putByteArray("Image", c.getBlob(2));
					extras.putString("Discovery", c.getString(3));
					extras.putString("Year", c.getString(4));
					extras.putString("Discoverer", c.getString(5));
					c.close();
					fragment.listener.startArticleActivity(extras);
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

	private class AnimatedSimpleCursorAdapter extends SimpleCursorAdapter {
	
		public AnimatedSimpleCursorAdapter(Context context, int layout,
				Cursor c, String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			
			ObjectAnimator.ofFloat(v, "alpha", 0f, 1f).setDuration(500).start();
			
			return v;
		}
	}
}
