package com.akaashiitkgp.sci_time.controller;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.akaashiitkgp.sci_time.R;
import com.akaashiitkgp.sci_time.view.viewgroup.TimelineFragment.ContentFragmentListener;
import com.akaashiitkgp.sci_time.view.viewgroup.LayoutContainer;
import com.akaashiitkgp.sci_time.view.viewgroup.TimelineFragment;

public class Main extends Activity implements OnClickListener, ContentFragmentListener {

	/**
	 * The application
	 */
	private Sci_Time application;
	/**
	 * The root layout of the activity.
	 */
	LayoutContainer root;
	/**
	 * The action settings image view will inflate the settings menu on click.
	 */
	ImageView menu;
	/**
	 * Header text
	 */
	TextView header;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		root = (LayoutContainer) this.getLayoutInflater().inflate(R.layout.main, null);
		
		setContentView(this.root);
		// Set up application
		application = (Sci_Time) getApplicationContext();
		
		// Set up fonts
		Typeface app_font = Typeface.createFromAsset(getAssets(), "fonts/neuropol_x.ttf");
		header = (TextView) findViewById(R.id.header);
		header.setTypeface(app_font);
		
		// Set up the settings button
		menu = (ImageView) findViewById(R.id.button_menu);
		menu.setOnClickListener(this);
		
		if(savedInstanceState != null) {
			return;
		}
		
		TimelineFragment firstFragment = new TimelineFragment();
		getFragmentManager().beginTransaction().add(R.id.fragmentContainer, firstFragment).commit();

	}

	// Click handlers for buttons in the view
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_menu:
			root.toggleMenu();
			break;
		default:
			break;
		}
	}
	
	// Setting click action for menu button.
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU) {
			root.toggleMenu();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public Cursor getYearRanges() {
		return application.getYearRanges();
	}

	@Override
	public Cursor getDiscoveries(String yearRange) {
		return application.getDiscoveries(yearRange);
	}
	
	// Disposing of everything
	@Override
	public void finish() {
		super.finish();
		application.closeDatabase();
	}

}