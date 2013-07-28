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
package com.aakashiitkgp.sci_time.controller;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.aakashiitkgp.sci_time.model.Database;

public class Sci_Time extends Application {
	/**
	 * The application database.
	 */
	private static Database sci_timeDatabase;
	/**
	 * The handler message id's.
	 */
	public static final int NO_TRANSMISSION_WAIT = 0;
	public static final int TRANSMIT_YEAR_RANGE = 1;
	public static final int TRANSMIT_DISCOVERIES = 2;
	public static final int TRANSMIT_ARTICLE = 3;
	/**
	 * The background working threads.
	 */
	private static Thread yearRangeThread;
	private static Thread discoveryThread;
	private static Thread articleThread;
	/**
	 * The applications TextToSpeech engine.
	 */
	private static TextToSpeech sci_timeTextToSpeech;
	
	// Initializes the application database instance.
	public static void initDatabase(Context context) {
		sci_timeDatabase = new Database(context);
	}
	
	// Initializes the application TextToSpeech engine instance.
	public static void initTextToSpeech (Context context) {
		sci_timeTextToSpeech = new TextToSpeech(context, new OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// Do nothing
			}
		});
	}
	
	// Converts text to speech via the TextToSpeech instance.  
	public static void speak (String text) {
		sci_timeTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	// Get year ranges in a worker thread.
	public static void getYearRanges(final Handler handler) {
		
		// Check whether the thread is currently active.
		if(yearRangeThread != null && yearRangeThread.isAlive()) {
			Message msg = handler.obtainMessage();
			msg.what = Sci_Time.NO_TRANSMISSION_WAIT;
			handler.sendMessage(msg);
			return;
		}
		
		// Initialize the worker thread.
		yearRangeThread =  new Thread() {
			@Override
			public void run() {
				// Obtain the messages from global pool of messages.
				Message msg = handler.obtainMessage();
				
				// Setup the objects to be sent to the UI thread.
				msg.what = Sci_Time.TRANSMIT_YEAR_RANGE;
				msg.obj = sci_timeDatabase.getYearRanges();
				
				// Send the message.
				handler.sendMessage(msg);	
			}
		};
		
		// Start the thread
		yearRangeThread.start();
	}
	
	// Get discoveries in a worker thread.
	public static void getDiscoveries(final Handler handler, final String yearRange) {
		
		// Check whether the thread is currently active.
		if(discoveryThread != null && discoveryThread.isAlive()) {
			Message msg = handler.obtainMessage();
			msg.what = Sci_Time.NO_TRANSMISSION_WAIT;
			handler.sendMessage(msg);
			return;
		}
		
		// Initialize the worker thread.	
		discoveryThread =  new Thread() {
			@Override
			public void run() {
				// Obtain the messages from global pool of messages.
				Message msg = handler.obtainMessage();
				
				// Setup the objects to be sent to the UI thread.
				msg.what = Sci_Time.TRANSMIT_DISCOVERIES;
				msg.obj = sci_timeDatabase.getDiscoveries(yearRange);
				
				// Send the message.
				handler.sendMessage(msg);	
			}
		};
		
		// Start the thread
		discoveryThread.start();
		
	}
	
	// Get article in a worker thread.
	public static void getArticle(final Handler handler, final String yearRange, final String title) {
		// Check whether the thread is currently active.
				if(articleThread != null && articleThread.isAlive()) {
					Message msg = handler.obtainMessage();
					msg.what = Sci_Time.NO_TRANSMISSION_WAIT;
					handler.sendMessage(msg);
					return;
				}
				
				// Initialize the worker thread.	
				articleThread =  new Thread() {
					@Override
					public void run() {
						// Obtain the messages from global pool of messages.
						Message msg = handler.obtainMessage();
						
						// Setup the objects to be sent to the UI thread.
						msg.what = Sci_Time.TRANSMIT_ARTICLE;
						msg.obj = sci_timeDatabase.getArticle(yearRange, title);
						
						// Send the message.
						handler.sendMessage(msg);	
					}
				};
				
				// Start the thread
				articleThread.start();
	}
	
	// Dispose-off everything.
	public static void close() {
		yearRangeThread = null;
		discoveryThread = null;
		articleThread = null;
		if (sci_timeTextToSpeech != null) {
			sci_timeTextToSpeech.stop();
			sci_timeTextToSpeech.shutdown();
		}
		sci_timeTextToSpeech = null;
		sci_timeDatabase.close();
		sci_timeDatabase = null;
	}	
}
