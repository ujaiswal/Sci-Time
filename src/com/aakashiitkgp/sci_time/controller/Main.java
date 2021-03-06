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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aakashiitkgp.sci_time.R;
import com.aakashiitkgp.sci_time.view.viewgroup.AboutFragment;
import com.aakashiitkgp.sci_time.view.viewgroup.LayoutContainer;
import com.aakashiitkgp.sci_time.view.viewgroup.MenuFragment.MenuFragmentListener;
import com.aakashiitkgp.sci_time.view.viewgroup.TimelineFragment;
import com.aakashiitkgp.sci_time.view.viewgroup.TimelineFragment.TimelineFragmentListener;

public class Main extends Activity implements OnClickListener, TimelineFragmentListener, MenuFragmentListener {

	/**
	 * The root layout of the activity.
	 */
	LayoutContainer root;
	/**
	 * The action settings image view will inflate the settings menu on click.
	 */
	ImageView menu;
	/**
	 * Header text
	 */
	TextView header;
	/**
	 * Constants
	 */
	private static final int CHECK_TTS = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setting up root view of the main activity.
		root = (LayoutContainer) this.getLayoutInflater().inflate(R.layout.main, null);
		setContentView(this.root);
		
		// Set up fonts
		Typeface app_font = Typeface.createFromAsset(getAssets(), "fonts/neuropol_x.ttf");
		header = (TextView) findViewById(R.id.header);
		header.setTypeface(app_font);
		
		// Set up the settings button
		menu = (ImageView) findViewById(R.id.button_menu);
		menu.setOnClickListener(this);
		
		// One-time initializations.
		if(savedInstanceState != null) {
			return;
		}
		
		// Initializing the application database.  
		Sci_Time.initDatabase(getApplicationContext());
		
		// Fire off an intent to check if a TTS engine is installed
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, CHECK_TTS);
		
        // Starting the timeline fragment as the first fragment.
		TimelineFragment fragment_timeline = new TimelineFragment();
		getFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment_timeline).commit();
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
	
	// Initialize TextToSpeech
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHECK_TTS)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                // success, create the TTS instance
        		Sci_Time.initTextToSpeech(getApplicationContext());
            }
            else
            {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
	}
	
	// Disposing of everything
	@Override
	public void finish() {
		super.finish();
		Sci_Time.close();
	}
	
	// Timeline fragment interface
	@Override
	public void getYearRanges(Handler handler) {
		Sci_Time.getYearRanges(handler);
	}

	@Override
	public void getDiscoveries(Handler handler, String yearRange) {
		Sci_Time.getDiscoveries(handler, yearRange);
	}
	
	@Override
	public void getArticle(Handler handler, String yearRange, String title) {
		Sci_Time.getArticle(handler, yearRange, title);
	}
	
	@Override
	public void startArticleActivity(Bundle extras) {
		Intent article_intent = new Intent(this, Article.class);
		article_intent.putExtras(extras);
		startActivity(article_intent);
	}
	
	// Menu fragment interface
	@Override
	public void changeFragment(int selectedFragment) {
		
		switch(selectedFragment) {
		case R.id.tab_timeline:
			TimelineFragment fragment_timeline = new TimelineFragment();
			getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment_timeline).commit();	
			break;
		case R.id.tab_discovery_tree:
			break;
		case R.id.tab_about:
			AboutFragment fragment_about = new AboutFragment();
			getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment_about).commit();
			break;
		default:
			break;
		}
	}

}