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
package com.aakashiitkgp.sci_time.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {
	/**
	 *  Database name and version to connect.
	 */
	private static final String DATABASE_NAME = "sci_time";
    private static final int DATABASE_VERSION = 1;
    
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Fetches the list of year ranges.
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
	
	// Fetches the discoveries in the selected year range.
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
