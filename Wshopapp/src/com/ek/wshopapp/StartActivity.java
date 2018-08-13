package com.ek.wshopapp;

import android.os.Bundle;
import android.widget.Toast;


public class StartActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		Toast.makeText(this, "Please log in by using the menu",
				Toast.LENGTH_LONG).show();
	}

}
