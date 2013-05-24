package com.akaashiitkgp.sci_time.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.akaashiitkgp.sci_time.R;
import com.akaashiitkgp.sci_time.view.viewgroup.LayoutContainer;

public class Main extends Activity implements OnClickListener {

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
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		root = (LayoutContainer) this.getLayoutInflater().inflate(R.layout.main, null);
		
		setContentView(this.root);
		
		// Set up the settings button
		menu = (ImageView) findViewById(R.id.button_menu);
		menu.setOnClickListener(this);
		
		// Set up list view.
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sections)));

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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	
}

