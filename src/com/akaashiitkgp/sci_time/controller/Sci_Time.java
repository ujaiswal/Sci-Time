package com.akaashiitkgp.sci_time.controller;

import com.akaashiitkgp.sci_time.model.Database;

import android.app.Application;
import android.database.Cursor;

public class Sci_Time extends Application {
	private Database sci_timeDatabase;
	
	public Cursor getYearRanges() {	
		return sci_timeDatabase.getTables();
	}
	
	public void initializeDatabase() {
			sci_timeDatabase = new Database(this);
	}
	
	public void closeDatabase () {
		sci_timeDatabase.close();
	}
}
