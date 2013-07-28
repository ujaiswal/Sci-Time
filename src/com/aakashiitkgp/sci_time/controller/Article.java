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

import com.aakashiitkgp.sci_time.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Article extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		
		Bundle articleData = getIntent().getExtras();
		
		Typeface app_font = Typeface.createFromAsset(getAssets(), "fonts/neuropol_x.ttf");
		
		TextView title = (TextView) findViewById(R.id.article_title);
		title.setText(articleData.getString("Title"));
		title.setTypeface(app_font);
		
		ImageView image = (ImageView) findViewById(R.id.article_image);
		byte[] articleImage = articleData.getByteArray("Image");
		if(articleImage != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(articleImage, 0, articleImage.length);
			image.setImageBitmap(bitmap);
		}
		
		TextView discovery = (TextView) findViewById(R.id.article_discovery);
		discovery.setText(articleData.getString("Discovery"));
		
		TextView year = (TextView) findViewById(R.id.article_year);
		year.setText(articleData.getString("Year"));
		
		TextView discoverer = (TextView) findViewById(R.id.article_discoverer);
		discoverer.setText(articleData.getString("Discoverer"));
		
	}
}
