package com.ek.wshopapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCategoryActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_category);

		Button button = (Button) findViewById(R.id.create);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CreateCategoryOnServer createCategoryOnServer = new CreateCategoryOnServer(
						ip);
				boolean Empty = true;
				EditText name = (EditText) findViewById(R.id.categorynameview);
				if (name.getText().toString().equals("")) {
					name.setError(" please write name");
					Empty = false;
				}
				createCategoryOnServer.setCategoryName(name.getText()
						.toString());
				if(Empty=true){
					createCategoryOnServer.execute();					
				}
			}
		});
	}

	class CreateCategoryOnServer extends AsyncTask<Void, Void, Boolean> {
		private String ip;
		private String categoryname;

		public CreateCategoryOnServer(String ip) {
			this.ip = ip;
		}

		public void setCategoryName(String categoryname) {
			this.categoryname = categoryname;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpPost post = new HttpPost("http://" + ip
						+ ":9000/category/showCategoryForm");
				DefaultHttpClient client = new DefaultHttpClient();

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("categoryname",
						categoryname));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				if (cookies != null) {
					for (Cookie cookie : cookies) {
						client.getCookieStore().addCookie(cookie);
					}
				}
				client.execute(post, new BasicResponseHandler());

				return true;
			} catch (Exception e) {
				Log.e("Error creating category", e.getMessage());

				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success == true) {
				Toast.makeText(getApplicationContext(), "Success!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
