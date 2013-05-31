package com.akaashiitkgp.sci_time.controller;

import com.akaashiitkgp.sci_time.model.Database;

import android.app.Application;
import android.database.Cursor;

public class Sci_Time extends Application {
	private Database sci_timeDatabase;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(sci_timeDatabase == null) {
			sci_timeDatabase = new Database(this);
		}
	}

	public Cursor getYearRanges() {	
		return sci_timeDatabase.getYearRanges();
	}

	public Cursor getDiscoveries(String yearRange) {
		return sci_timeDatabase.getDiscoveries(yearRange);
	}
	
	public void closeDatabase() {
		sci_timeDatabase.close();
	}	
}
