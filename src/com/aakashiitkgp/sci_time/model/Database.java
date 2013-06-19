package com.aakashiitkgp.sci_time.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {
	
	private static final String DATABASE_NAME = "sci_time";
    private static final int DATABASE_VERSION = 1;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public Cursor getYearRanges() {
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"0 _id", "Name"};
		String sqlTable = "list";
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getDiscoveries(String yearRange) {
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"0 _id", "Year", "Discovery", "Discoverer"};
		String sqlTable = "[" + yearRange + "]";
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
		c.moveToFirst();
		
		return c;
	}

}
