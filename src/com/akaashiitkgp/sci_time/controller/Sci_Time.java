package com.akaashiitkgp.sci_time.controller;

import com.akaashiitkgp.sci_time.model.Database;

import android.app.Application;

public class Sci_Time extends Application {
	private Database sci_timeDatabase;
	
	public Database getDatabase() {	
		return sci_timeDatabase;
	}
	
	public void initializeDatabase() {
		sci_timeDatabase = new Database(this);
	}
}
