package com.akaashiitkgp.sci_time.controller;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.akaashiitkgp.sci_time.R;
import com.akaashiitkgp.sci_time.model.Database;
import com.akaashiitkgp.sci_time.view.viewgroup.LayoutContainer;

public class Main extends Activity implements OnClickListener {

	/**
	 * The application
	 */
	Sci_Time application;
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
	/**
	 * The list view for tables.
	 */
	ListView tableList;
	/**
	 * Button for fetching table list.
	 */
	Button button_getTables;
	/**
	 * Adapter for list view.
	 */
	SimpleCursorAdapter tableListAdapter;
	/**
	 * Header text
	 */
	TextView header;
	/**
	 * Database to be used in the activity
	 */
	Database sci_timeDb;
	
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
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sections)));
		
		// Set up list view for tables.
		tableList = (ListView) findViewById(R.id.TableList);
		application.initializeDatabase();
		tableListAdapter = new SimpleCursorAdapter(this, R.layout.table_list, application.getDatabase().getTables(), new String [] {"Name"}, new int [] {R.id.TableListItem}, 0);
		tableList.setAdapter(tableListAdapter);
		
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
}