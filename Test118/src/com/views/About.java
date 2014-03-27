package com.views;

import android.app.Activity;
import android.os.Bundle;

/**
 * This is the activity class responsible for creating and handling actions in
 * about.xml.
 * 
 * @author Xing Wei(david.wx@foxmail.com)
 */

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);
	}
}
