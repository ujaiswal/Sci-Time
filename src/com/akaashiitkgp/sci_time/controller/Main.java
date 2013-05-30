package com.akaashiitkgp.sci_time.controller;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.akaashiitkgp.sci_time.R;
import com.akaashiitkgp.sci_time.view.viewgroup.LayoutContainer;

public class Main extends Activity implements OnClickListener {

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
	 * The list view of menu.
	 */
	ListView menuList;
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
		
		
		// Set up header
		header = (TextView) findViewById(R.id.header);
		header.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/neuropol_x.ttf"));
		
		// Set up the settings button
		menu = (ImageView) findViewById(R.id.button_menu);
		menu.setOnClickListener(this);
		
		// Set up list view of menu.
		menuList = (ListView) findViewById(R.id.list);
		menuList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sections)));
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
}