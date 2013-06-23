package com.aakashiitkgp.sci_time.controller;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.aakashiitkgp.sci_time.model.Database;

public class Sci_Time extends Application {
	private static Database sci_timeDatabase;
	public static final int NO_TRANSMISSION_WAIT = 0;
	public static final int TRANSMIT_YEAR_RANGE = 1;
	public static final int TRANSMIT_DISCOVERIES = 2;
	private static Thread yearRangeThread;
	private static Thread discoveryThread;
	private static TextToSpeech sci_timeTextToSpeech;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static void initDatabase(Context context) {
		sci_timeDatabase = new Database(context);
	}
	
	public static void initTextToSpeech (Context context) {
		sci_timeTextToSpeech = new TextToSpeech(context, new OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// Do nothing
			}
		});
	}
	
	public static void speak (String text) {
		sci_timeTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	public static void getYearRanges(final Handler handler) {
		
		if(yearRangeThread != null && yearRangeThread.isAlive()) {
			Message msg = handler.obtainMessage();
			msg.what = Sci_Time.NO_TRANSMISSION_WAIT;
			handler.sendMessage(msg);
			return;
		}
			
		yearRangeThread =  new Thread() {
			@Override
			public void run() {		
				Message msg = handler.obtainMessage();
				msg.what = Sci_Time.TRANSMIT_YEAR_RANGE;
				msg.obj = sci_timeDatabase.getYearRanges();
				handler.sendMessage(msg);	
			}
		};
		
		yearRangeThread.start();
	}

	public static void getDiscoveries(final Handler handler, final String yearRange) {
		
		if(discoveryThread != null && discoveryThread.isAlive()) {
			Message msg = handler.obtainMessage();
			msg.what = Sci_Time.NO_TRANSMISSION_WAIT;
			handler.sendMessage(msg);
			return;
		}
			
		discoveryThread =  new Thread() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = Sci_Time.TRANSMIT_DISCOVERIES;
				msg.obj = sci_timeDatabase.getDiscoveries(yearRange);
				handler.sendMessage(msg);	
			}
		};
		
		discoveryThread.start();
		
	}
	
	public static void close() {
		yearRangeThread = null;
		discoveryThread = null;
		if (sci_timeTextToSpeech != null) {
			sci_timeTextToSpeech.stop();
			sci_timeTextToSpeech.shutdown();
		}
		sci_timeTextToSpeech = null;
		sci_timeDatabase.close();
		sci_timeDatabase = null;
	}	
}
